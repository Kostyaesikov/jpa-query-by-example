/*
 * Copyright 2015 JAXIO http://www.jaxio.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaxio.jpa.querybyexample;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.hibernate.proxy.HibernateProxyHelper.getClassWithoutInitializingProxy;

@Named
@Singleton
public class RepositoryLocator {
    private Map<Class<?>, GenericRepository<?, ?>> repositories = newHashMap();

    @Inject
    void buildCache(List<GenericRepository<?, ?>> registredRepositories) {
        for (GenericRepository<?, ?> repository : registredRepositories) {
            repositories.put(repository.getType(), repository);
        }
    }

    @SuppressWarnings("unchecked")
    public <PK extends Serializable, E extends Identifiable<PK>> GenericRepository<E, PK> getRepository(Class<? extends E> clazz) {
        return (GenericRepository<E, PK>) repositories.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <PK extends Serializable, E extends Identifiable<PK>> GenericRepository<E, PK> getRepository(E entity) {
        return (GenericRepository<E, PK>) repositories.get(getClassWithoutInitializingProxy(entity));
    }
}