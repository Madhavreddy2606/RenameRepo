/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.gradle.tasks.aot;

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.work.DisableCachingByDefault;

/**
 * Specialization of {@link JavaExec} to be used as a base class for tasks that perform
 * ahead-of-time processing.
 *
 * @author Andy Wilkinson
 * @since 3.0.0
 */
@DisableCachingByDefault(because = "Cacheability can only be determined by a concrete implementation")
public abstract class AbstractAot extends JavaExec {

	private final DirectoryProperty sourcesDir;

	private final DirectoryProperty resourcesDir;

	private final DirectoryProperty classesDir;

	private final Property<String> groupId;

	private final Property<String> artifactId;

	protected AbstractAot() {
		this.sourcesDir = getProject().getObjects().directoryProperty();
		this.resourcesDir = getProject().getObjects().directoryProperty();
		this.classesDir = getProject().getObjects().directoryProperty();
		this.groupId = getProject().getObjects().property(String.class);
		this.artifactId = getProject().getObjects().property(String.class);
	}

	@Input
	public final Property<String> getGroupId() {
		return this.groupId;
	}

	@Input
	public final Property<String> getArtifactId() {
		return this.artifactId;
	}

	@OutputDirectory
	public final DirectoryProperty getSourcesDir() {
		return this.sourcesDir;
	}

	@OutputDirectory
	public final DirectoryProperty getResourcesDir() {
		return this.resourcesDir;
	}

	@OutputDirectory
	public final DirectoryProperty getClassesDir() {
		return this.classesDir;
	}

	List<String> processorArgs() {
		List<String> args = new ArrayList<>();
		args.add(getSourcesDir().getAsFile().get().getAbsolutePath());
		args.add(getResourcesDir().getAsFile().get().getAbsolutePath());
		args.add(getClassesDir().getAsFile().get().getAbsolutePath());
		args.add(getGroupId().get());
		args.add(getArtifactId().get());
		args.addAll(super.getArgs());
		return args;
	}

}
