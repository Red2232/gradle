/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.internal.component.external.model;

import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.internal.component.model.DependencyMetadata;
import org.gradle.internal.component.model.ModuleSource;

import java.util.List;

public interface MutableModuleComponentResolveMetadata extends ModuleComponentResolveMetadata {
    /**
     * Creates an immutable copy of this meta-data.
     */
    ModuleComponentResolveMetadata asImmutable();

    /**
     * Also sets the {@link #getId()} based on the provided component id.
     */
    void setComponentId(ModuleComponentIdentifier componentId);

    void setChanging(boolean changing);
    void setStatus(String status);
    void setStatusScheme(List<String> statusScheme);

    void setSource(ModuleSource source);

    /**
     * Replaces the dependencies of this module version.
     */
    void setDependencies(Iterable<? extends DependencyMetadata> dependencies);

    /**
     * Replaces the artifacts of this module version.
     */
    void setArtifacts(Iterable<? extends ModuleComponentArtifactMetadata> artifacts);
}
