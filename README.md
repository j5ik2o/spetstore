# Scala Pet Store API (開発中)
- Scala 2.10.x
- DDDに準拠した設計方針
- REST like API


## エンティティ(集約)と値オブジェクト
- [Account](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/account/Account.scala)
    - AccountStatus
    - AccountProfile
    - AccountConfig
- [Category](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/item/Category.scala)
- [ItemType](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/item/ItemType.scala)
- [Item](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/item/Item.scala)
- [Cart](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/purchase/Cart.scala)
    - CartItem
- [Order](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/domain/purchase/Order.scala)
    - OrderItem

[CartSpec](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/j5ik2o/spetstore/domain/purchase/CartSpec.scala), [OrderSpec](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/j5ik2o/spetstore/domain/purchase/OrderSpec.scala)あたりがみどころ。


## DDD基盤コード
- [Entity](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/infrastructure/support/Entity.scala)
- [Repository](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/infrastructure/support/Repository.scala)
    - [RepositoryOnJDBC](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/infrastructure/support/RepositoryOnJDBC.scala)  
    JDBCに対応したリポジトリの骨格実装。ScalikeJDBCで実装。
    - [RepositoryOnMemory](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/j5ik2o/spetstore/infrastructure/support/RepositoryOnMemory.scala)  
    メモリに対応したリポジトリの骨格実装。
    - RepositoryOnMemcached(TODO)  
    Memcachedに対応したリポジトリの骨格実装。
    - CacheManagementRepository(TODO)  
    キャッシュのマネジメントを行うリポジトリ実装。

## アプリケーション層の実装
- コントローラ
- アプリケーションサービス
