# Android客户端文档

### app工程
* [app](app) 该目录为阅读app，内部实现各个马甲包的差异化设计
* [app_reachmax](app_reachmax) 该目录为投放工具app
* [app_itools](CheatSheet) 该目录为开发使用的辅助工具app

---
以下文档都是阅读app工程的


### 分支说明
main      主分支
develop   通用开发分支
feature   功能开发分支


### 组件化 + 模块化

组件化分层，从上往下：
1. app层。在 [app](app) 目录
2. 各个模块层。在 [layer_module](layer_module) 目录
3. 马甲包ui层。在 [layer_channel](layer_channel) 目录
4. base层。在 [layer_base](layer_base) 目录

依赖关系说明：
* 每一层都依赖该层以下的所有组件；
* 推荐使用模块化设计，将每块相对独立的业务放在 layer_module层的 独立组件里。
* 推荐使用拆分接口和实现的方式来解决同层级组件的依赖关系，将依赖的接口放在底层，实现放在上层。


### 渠道打包说明
现在 local.properties 中设置flavor。

* 打蒲公英包
  
  执行task： Tasks/upload/ 目录下 uploadApkFor{FlavorBuildType}
  该task会将打完的包上传到蒲公英内测平台上。

* 打play包

  执行task： Tasks/play/ 目录下 bundleAabFor{FlavorBuildType}
  打完包后，aab文件在工程目录中的 /apks/ 目录下。


### apks目录
* /apks/doc/ 目录下存放文档
* /apks/{flavor}/ 目录下存放历史打包


### document目录
* [jniLibs](document/jniLibs) 目录
  存放play包用的正式so



### OpenCode放置文件
为每个技能名称创建一个文件夹，并在其中放入 SKILL.md。 OpenCode 会搜索以下位置：

项目配置：.opencode/skills/<name>/SKILL.md
全局配置：~/.config/opencode/skills/<name>/SKILL.md
项目 Claude 兼容：.claude/skills/<name>/SKILL.md
全局 Claude 兼容：~/.claude/skills/<name>/SKILL.md
项目代理兼容：.agents/skills/<name>/SKILL.md
全局代理兼容：~/.agents/skills/<name>/SKILL.md

# 1Tang
