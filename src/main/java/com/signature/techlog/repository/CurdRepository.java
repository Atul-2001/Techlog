package com.signature.techlog.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface CurdRepository<T, ID> {

    Logger LOGGER = LogManager.getLogger(CurdRepository.class);

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return {@literal true} if given entity were saved successfully, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     */
    boolean save(T entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @return {@literal true} if given entities were saved successfully, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@link Iterable entities} or one of its entities is
     *           {@literal null}.
     */
    boolean saveAll(Iterable<T> entities);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    T findById(ID id);

    /**
     * Returns whether an entity with the given id exists.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
     * @throws IllegalArgumentException if {@literal id} is {@literal null}.
     */
    boolean existsById(ID id);

    /**
     * Returns all instances of the type.
     *
     * @return all entities
     */
    Iterable<T> findAll();

    /**
     * Returns all instances of the type {@code T} with the given IDs.
     * <p>
     * If some or all ids are not found, no entities are returned for these IDs.
     * <p>
     * Note that the order of elements in the result is not guaranteed.
     *
     * @param ids must not be {@literal null} nor contain any {@literal null} values.
     * @return guaranteed to be not {@literal null}. The size can be equal or less than the number of given
     *         {@literal ids}.
     * @throws IllegalArgumentException in case the given {@link Iterable ids} or one of its items is {@literal null}.
     */
    Iterable<T> findAllById(Iterable<ID> ids);

    /**
     * Returns the number of entities available.
     *
     * @return the number of entities.
     */
    long count();

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @return {@literal true} if an entity with the given id were deleted successfully, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@literal id} is {@literal null}
     */
    boolean deleteById(ID id);

    /**
     * Deletes a given entity.
     *
     * @param entity must not be {@literal null}.
     * @return {@literal true} if given entity were deleted successfully, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    boolean delete(T entity);

    /**
     * Deletes all instances of the type {@code T} with the given IDs.
     *
     * @param ids must not be {@literal null}. Must not contain {@literal null} elements.
     * @return {@literal true} if an entity with the given ids were deleted successfully, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@literal ids} or one of its elements is {@literal null}.
     * @since 2.5
     */
    boolean deleteAllById(Iterable<ID> ids);

    /**
     * Deletes the given entities.
     *
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     * @return {@literal true} if given entities were deleted successfully, {@literal false} otherwise.
     * @throws IllegalArgumentException in case the given {@literal entities} or one of its entities is {@literal null}.
     */
    boolean deleteAll(Iterable<T> entities);

    /**
     * Deletes all entities managed by the repository.
     * Note : This method has currently no implementation, because I have no idea how to implement it.
     *        Please avoid its uses.
     * @return {@literal true} if all entities were deleted successfully, {@literal false} otherwise.
     */
    boolean deleteAll();
}