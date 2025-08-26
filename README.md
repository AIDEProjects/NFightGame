# NFightGame

## [Java Doc](readme/docs/javadoc/index.html) 仅本地

## 待办
- [ ] 创建拥有基本属性的物体GObject 
- [ ] 创建基于GObject实现锚点的角色渲染器 
- [ ] 创建可以拥有组合功能的组合物体CompoundObject 
- [ ] 图片角色渲染: 提供位移, 翻转, 缩放操作 
- [ ] 动画角色渲染: 提供是否循环参数, 播放速率 
- [ ] 动画组角色渲染: 设置初始动画, 主动切换动画, 获取当前动画, 获取某动画  

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
