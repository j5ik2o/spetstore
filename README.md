# Scala Pet Store API (開発中)

[![Build Status](https://travis-ci.org/j5ik2o/spetstore.png?branch=master)](https://travis-ci.org/j5ik2o/spetstore)

## 目的
DDDに基づいた、一つの実装を示すこと(DDDは設計思想なので具体的な実装方式は複数あり得ますが、私が考える最良の実装という意味)。

## 特徴
- Scala 2.10.x
- DDDに準拠した設計方針
- 対象ドメインはペットストア
- アプリケーションとしては REST like API

## 対象ドメイン
- ペットストア
- 原典は [Java Pet Store](http://www.oracle.com/technetwork/java/petstore1-3-1-02-139690.html)ですが、独自にアレンジしている部分があります。
- 参考にした実装は [mybatis/jpetstore-6](https://github.com/mybatis/jpetstore-6)

## レイヤー構造
DDDのレイヤー化アーキテクチャに従い、次のとおりのレイヤーに分割します。

- アプリケーション層
- ドメイン層
- インフラストラクチャ層

もっとも重要なのは、メンタルモデルを反映したドメイン層です。
このドメイン層を、非ドメイン層の知識から侵蝕されることを防ぐために、隔離します。

## ドメイン層

### エンティティ(集約)と値オブジェクト
- [Account](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/account/Account.scala) = ペットストアの顧客
    - [AccountStatus](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/account/AccountStatus.scala)
    - [AccountProfile](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/account/AccountProfile.scala)
    - [AccountConfig](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/account/AccountConfig.scala)
- [Category](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/item/Category.scala) = 商品区分のカテゴリ(ex. 犬)
- [ItemType](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/item/ItemType.scala) = 商品区分(ex. 柴犬)
- [Item](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/item/Item.scala) = 商品(ex. ぽち)
- [Cart](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/Cart.scala) = ショッピングカート
    - [CartItem](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/CartItem.scala) = カート内の商品と数量
- [Order](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/Order.scala) = 注文
    - [OrderItem](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/OrderItem.scala) = 注文する商品と数量

- Spec  
[CartSpec](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/github/j5ik2o/spetstore/domain/purchase/CartSpec.scala), [OrderSpec](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/github/j5ik2o/spetstore/domain/purchase/OrderSpec.scala)あたりがみどころ。

ここで重要なのは、モデルの表現にユビキタス言語以外の言葉を利用しないことです。実装技術の知識は含めてはいけません。それは他の層です。

### DDD基盤コード
わかりやすくするために、特別なライブラリを用意せず、簡単な基盤コードを含めています。
- [Entity](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/Entity.scala) = DDDにおけるエンティティの責務
- [Repository](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/Repository.scala) = DDDにおけるリポジトリの責務
    - [RepositoryOnJDBC](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/RepositoryOnJDBC.scala)  
    JDBCに対応したリポジトリの骨格実装。[ScalikeJDBC](http://scalikejdbc.org/)で実装。
    - [RepositoryOnMemory](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/RepositoryOnMemory.scala)  
    メモリに対応したリポジトリの骨格実装。内部実装はMapですがRepositoryとして操作できる。
    - RepositoryOnMemcached(TODO)  
    Memcachedに対応したリポジトリの骨格実装。
    - CacheManagementRepository(TODO)  
    キャッシュのマネジメントを行うリポジトリ実装。

## アプリケーション層
- コントローラ(TODO)
- アプリケーションサービス(TODO)
