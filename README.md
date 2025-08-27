# NFightGame

## [Java Doc](readme/docs/javadoc/index.html) 仅本地

## 待办
- [ ] 当角色添加或移除某个组件时， 自动调用相关静态系统的代码，将角色加入某类型系统管理列表中或从中移除，来达到自动识别并运行系统的目的

## 项目配置
- `libgdx`-1.12.1
  - `freetype`-1.12.1
- ``idea``-2024.3
- `java`-1.8
- `gradle`
  - `gradle`-7.6.2
  - `AGP`-7.4.1
- `android`
  - `compileSdk` 30
  - `targetSdk` 34
  - `minSdk` 21

## 项目进度
### 0.1.0 实现基于锚点渲染图片角色
1. 渲染图片角色
2. 给定锚点(脚底)坐标渲染角色精准站在地板上
3. 可控制位移, 缩放, 翻转

### 0.1.1 封装图像角色与渲染器
1. 制作一个图像角色类用于装下角色各属性: 位置, 翻转, 缩放, 图像
2. 制作图像角色渲染器专门渲染图像角色

### 0.1.2
1. 将绘制坐标转换与翻转等处理封装为方法并将draw封装到role自身, 取消了渲染器

### 0.1.3
1. 实现角色圆形攻击范围的调试线绘制

### 0.1.4
1. 增加动画器角色类

### 0.2.0
1. 对现有类进行再封装
2. 封装了GObject作为组件集合体， TextureComponent为材质组件拥有相关属性方法，以及TextureRenderer独立处理图像渲染

### 0.3.0
1. 规范化代码结构：抽象出IComponent IRenderer接口与其实现类
2. 将基本属性封装到TransformComponent并默认绑定GObject创建时生成
3. 独立开transfrom，textureComp，animatorComp, circleColliderComp等组件属性方法结构
4. 设置单独debugRenderer用于调试线渲染

#### 0.3.1
1. 增加ui_skin等资源以及配套工具类
