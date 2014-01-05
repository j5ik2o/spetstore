# Scala Pet Store API (開発中)

[![Build Status](https://travis-ci.org/j5ik2o/spetstore.png?branch=master)](https://travis-ci.org/j5ik2o/spetstore)

## 目的
DDDに基づいた、一つの実装を示すこと(DDDは設計思想なので具体的な実装方式は複数あり得ますが、私が考える最良の実装という意味)。

## 特徴
- Scala 2.10対応
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

### ドメインモデル
- [Customer Module](https://github.com/j5ik2o/spetstore/tree/master/src/main/scala/com/github/j5ik2o/spetstore/domain/customer) = 顧客モジュール
    - [Customer](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/customer/Customer.scala) = ペットストアの顧客
        - [CustomerStatus](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/customer/CustomerStatus.scala)
        - [CustomerProfile](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/customer/CustomerProfile.scala)
        - [CustomerConfig](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/customer/CustomerConfig.scala)
- [Pet Module](https://github.com/j5ik2o/spetstore/tree/master/src/main/scala/com/github/j5ik2o/spetstore/domain/pet) = ペットモジュール
    - [Category](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/pet/Category.scala) = ペットのカテゴリ(ex. 犬)
    - [PetType](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/pet/PetType.scala) = ペットの品種(ex. 柴犬)
    - [Pet](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/pet/Pet.scala) = ペット(ex. ぽち)
- [Purchase Module](https://github.com/j5ik2o/spetstore/tree/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase) = 購買モジュール
    - [Cart](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/Cart.scala) = ショッピングカート
        - [CartItem](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/CartItem.scala) = カート内の商品と数量
    - [Order](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/Order.scala) = 注文
        - [OrderItem](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/domain/purchase/OrderItem.scala) = 注文する商品と数量

### Specs
とりあえず、重要なところだけSpecを書いています。
- [CartSpec](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/github/j5ik2o/spetstore/domain/purchase/CartSpec.scala)
- [OrderSpec](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/github/j5ik2o/spetstore/domain/purchase/OrderSpec.scala)

ここで重要なのは、モデルの表現(クラス名、属性名、振る舞いの名前(引数・戻り値も))にユビキタス言語以外の言葉を利用しないことです。  
これらの要素に、実装技術の言葉は、ドメイン層に含めてはいけません(実装技術の言葉を含めてしまうとメンタルモデルが離れていきドメインについて理解することが難しくなるため。ただし、StringやIntなどのデータ型や、ListやMap, Try, Option, Futureなどのコンテナ型は例外とする)。実装技術に関する知識は、アプリケーション層かインフラストラクチャ層に対応づけましょう。

## インフラストラクチャ層
### DDD基盤コード
わかりやすくするために、特別なライブラリを用意せず、簡単な基盤コードを含めています。
- [Entity](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/Entity.scala) = DDDにおけるエンティティの責務
- [Repository](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/Repository.scala) = DDDにおけるリポジトリの責務
    - [RepositoryOnJDBC](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/RepositoryOnJDBC.scala)  
    JDBCに対応したリポジトリの骨格実装。[ScalikeJDBC](http://scalikejdbc.org/)で実装。Specは[こちら](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/github/j5ik2o/spetstore/infrastructure/support/RepositoryOnJDBCSpec.scala)
    - [RepositoryOnMemory](https://github.com/j5ik2o/spetstore/blob/master/src/main/scala/com/github/j5ik2o/spetstore/infrastructure/support/RepositoryOnMemory.scala)  
    メモリに対応したリポジトリの骨格実装。内部実装はMapですがRepositoryとして操作できる。Specは[こちら](https://github.com/j5ik2o/spetstore/blob/master/src/test/scala/com/github/j5ik2o/spetstore/infrastructure/support/RepositoryOnMemorySpec.scala)
    - RepositoryOnMemcached(TODO)  
    Memcachedに対応したリポジトリの骨格実装。
    - CacheManagementRepository(TODO)  
    キャッシュのマネジメントを行うリポジトリ実装。

## アプリケーション層
- コントローラ(TODO)
- アプリケーションサービス(TODO)
