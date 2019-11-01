package org.gradle.kotlin.dsl.provider

import org.gradle.api.Action
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.precompile.v1.PrecompiledInitScript
import org.gradle.kotlin.dsl.precompile.v1.PrecompiledProjectScript
import org.gradle.kotlin.dsl.precompile.v1.PrecompiledSettingsScript
import org.gradle.kotlin.dsl.support.CompiledKotlinBuildScript
import org.gradle.kotlin.dsl.support.CompiledKotlinBuildscriptAndPluginsBlock
import org.gradle.kotlin.dsl.support.CompiledKotlinInitScript
import org.gradle.kotlin.dsl.support.CompiledKotlinSettingsScript
import org.gradle.kotlin.dsl.support.CompiledKotlinSettingsPluginManagementBlock
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.valueParameters
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.jvmErasure


class ScriptApiTest {

    @Test
    fun `IDE build script template implements script api`() =
        assertScriptApiOf<KotlinBuildScript>()

    @Test
    fun `IDE settings script template implements script api`() =
        assertScriptApiOf<KotlinSettingsScript>()

    @Test
    fun `IDE init script template implements script api`() =
        assertScriptApiOf<KotlinInitScript>()

    @Test
    fun `compiled init script template implements script api`() =
        assertScriptApiOf<CompiledKotlinInitScript>()

    @Test
    fun `compiled settings script template implements script api`() =
        assertScriptApiOf<CompiledKotlinSettingsScript>()

    @Test
    fun `compiled settings pluginManagement block template implements script api`() =
        assertScriptApiOf<CompiledKotlinSettingsPluginManagementBlock>()

    @Test
    fun `compiled project script template implements script api`() =
        assertScriptApiOf<CompiledKotlinBuildScript>()

    @Test
    fun `compiled project buildscript and plugins block template implements script api`() =
        assertScriptApiOf<CompiledKotlinBuildscriptAndPluginsBlock>()

    @Test
    fun `precompiled project script template implements script api`() =
        assertScriptApiOf<PrecompiledProjectScript>()

    @Test
    fun `precompiled settings script template implements script api`() =
        assertScriptApiOf<PrecompiledSettingsScript>()

    @Test
    fun `precompiled init script template implements script api`() =
        assertScriptApiOf<PrecompiledInitScript>()
}


private
inline fun <reified T> assertScriptApiOf() {
    if (!KotlinScript::class.java.isAssignableFrom(T::class.java))
        assertApiOf<T>(KotlinScript::class)
}


private
inline fun <reified T> assertApiOf(expectedApi: KClass<*>) =
    assertThat(
        expectedApi.apiMembers.missingMembersFrom(T::class),
        equalTo(emptyList())
    )


private
typealias ScriptApiMembers = Collection<KCallable<*>>


private
val KClass<*>.apiMembers: ScriptApiMembers
    get() = declaredMembers


private
fun ScriptApiMembers.missingMembersFrom(scriptTemplate: KClass<*>): List<KCallable<*>> =
    filterNot(scriptTemplate.publicMembers::containsMemberCompatibleWith)


private
val KClass<*>.publicMembers
    get() = members.filter { it.visibility == KVisibility.PUBLIC }


private
fun List<KCallable<*>>.containsMemberCompatibleWith(api: KCallable<*>) =
    find { it.isCompatibleWith(api) } != null


private
fun KCallable<*>.isCompatibleWith(api: KCallable<*>) =
    when (this) {
        is KFunction -> isCompatibleWith(api)
        is KProperty -> isCompatibleWith(api)
        else -> false
    }


private
fun KProperty<*>.isCompatibleWith(api: KCallable<*>) =
    this::class == api::class
        && name == api.name
        && returnType == api.returnType


private
fun KFunction<*>.isCompatibleWith(api: KCallable<*>) =
    when {
        api is KProperty && api !is KMutableProperty && isCompatibleWithGetterOf(api) -> true
        api is KFunction && isCompatibleWith(api) -> true
        else -> false
    }


private
fun KFunction<*>.isCompatibleWithGetterOf(api: KProperty<*>) =
    name == api.javaGetter?.name
        && returnType == api.getter.returnType
        && valueParameters.isEmpty() && api.getter.valueParameters.isEmpty()


private
fun KFunction<*>.isCompatibleWith(api: KFunction<*>) =
    name == api.name
        && returnType == api.returnType
        && valueParameters.isCompatibleWith(api.valueParameters)


private
fun List<KParameter>.isCompatibleWith(api: List<KParameter>) =
    when {
        size != api.size -> false
        isEmpty() -> true
        else -> (0 until size).all { idx -> this[idx].isCompatibleWith(api[idx]) }
    }


private
fun KParameter.isCompatibleWith(api: KParameter) =
    when {
        isVarargCompatibleWith(api) -> true
        isGradleActionCompatibleWith(api) -> true
        type.isParameterTypeCompatibleWith(api.type) -> true
        else -> false
    }


private
fun KParameter.isGradleActionCompatibleWith(api: KParameter) =
    api.type.jvmErasure == Action::class
        && isSamWithReceiverReturningUnit()
        && api.type.arguments[0].type!!.isTypeArgumentCompatibleWith(type.arguments[0].type!!)


private
fun KParameter.isSamWithReceiverReturningUnit() =
    type.jvmErasure == Function1::class
        && type.arguments[1] == KTypeProjection(KVariance.INVARIANT, Unit::class.createType())


private
fun KParameter.isVarargCompatibleWith(api: KParameter) =
    isVararg && api.isVararg && type.isParameterTypeCompatibleWith(api.type)


private
fun KType.isParameterTypeCompatibleWith(apiParameterType: KType) =
    when {
        this == apiParameterType -> true
        classifier != apiParameterType.classifier -> false
        hasCompatibleTypeArguments(apiParameterType) -> true
        else -> false
    }


private
fun KType.hasCompatibleTypeArguments(api: KType) =
    arguments.size == api.arguments.size && arguments.indices.all { idx ->
        arguments[idx].type!!.isTypeArgumentCompatibleWith(api.arguments[idx].type!!)
    }


private
fun KType.isTypeArgumentCompatibleWith(api: KType) =
    withNullability(false) == api
