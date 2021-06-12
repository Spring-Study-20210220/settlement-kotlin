// package settlement.kotlin
//
// import org.springframework.stereotype.Component
// import org.springframework.transaction.annotation.Transactional
// import javax.persistence.Entity
// import javax.persistence.EntityManager
//
// @Component
// class DataCleanUp(
//    private val entityManager: EntityManager
// ) {
//
//    private val tableNames: List<String> =
//        entityManager.metamodel.entities.filter { e ->
//            e.javaType.getAnnotation(Entity::class.java) != null
//        }.map { e ->
//            var res = ""
//            for (s in e.name) {
//                if (s.isUpperCase()) {
//                    res += "_" + s.toLowerCase()
//                } else {
//                    res += s
//                }
//            }
//            res
//        }
//
//    @Transactional
//    fun execute() {
//        entityManager.flush()
//        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
//
//        for (tableName in tableNames) {
//            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate()
//            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1")
//                .executeUpdate()
//        }
//
//        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
//    }
// }
