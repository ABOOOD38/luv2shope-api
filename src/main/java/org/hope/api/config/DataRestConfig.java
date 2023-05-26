package org.hope.api.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import org.hope.api.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataRestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    private static void disableHttpMethods(RepositoryRestConfiguration config, Class<?> clazz, HttpMethod[] unsupportedActions) {
        config.getExposureConfiguration().forDomainType(clazz)
                .withItemExposure(((metadata, httpMethods) -> httpMethods.disable(unsupportedActions)))
                .withCollectionExposure(((metadata, httpMethods) -> httpMethods.disable(unsupportedActions)));
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        final HttpMethod[] unsupportedActions = {HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.PATCH};

        //disable HTTP methods for Product: POST, DELETE, and PUT
        disableHttpMethods(config, Product.class, unsupportedActions);

        //disable HTTP methods for ProductCategory: POST, DELETE, and PUT
        disableHttpMethods(config, ProductCategory.class, unsupportedActions);

        disableHttpMethods(config, Country.class, unsupportedActions);

        disableHttpMethods(config, State.class, unsupportedActions);

        disableHttpMethods(config, Order.class, unsupportedActions);

        exposeIds(config);

        cors.addMapping("%s/**".formatted(config.getBasePath())).allowedOrigins(allowedOrigins);

        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //expose entities ids

        //get a list of all entity classes from the entity manager
        Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();

        //array of entity types
        List<Class<?>> entityClasses = new ArrayList<>();

        //get the java entity types for each entity
        entityTypes.forEach((entityType -> {
            entityClasses.add(entityType.getJavaType());
        }));

        //expose the entity ids for the array of type entity/domain types
        Class<?>[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);

    }
}
