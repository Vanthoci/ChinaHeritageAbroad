## 前台模板

网站前台模板位于`/src/main/webapp/templates`目录，使用`Freemarker`技术。通过修改模板文件，可以完全控制网站页面显示的内容。也可使用Vue(React)+API的方式制作网站。

## 后端技术

* Spring Boot：提供了对Spring开箱即用的功能。简化了Spring配置，提供自动配置auto-configuration功能。
* Spring MVC：MVC框架，使用方便，Bug较少。
* Spring Security：安全组件。
* MyBatis：持久化框架。
* Lucene：全文检索组件。

## 后台前端技术

* TypeScript: JavaScript的一个超集。
* Vue3：JavaScript框架。
* ElementPlus：Vue 3 UI 框架。
* Vite: 下一代前端开发与构建工具。
* Tailwind CSS: 功能类优先的 CSS 框架。
* VueRouter: Vue 路由组件。
* VueI18n: Vue 国际化组件。
* Tinymce: 富文本编辑器。

## 网站前端技术

网站前端技术可以使用传统的HTML（Freemarker），通过UJCMS提供的Freemarker的自定义标签获取数据；也可以使用前后端分离的vue或react，通过调用API获取数据。

由于前后端分离的方式对搜索引擎不友好，对于需要通过搜索引擎推广的网站不建议采用。

演示站使用传统的HTML（Freemarker），使用到以下一些技术：

* Freemarker
* HTML、CSS、JS
* Bootstrap
* JQuery
* Axios

## 目录结构

* src
  * main
    * java
      * com/ujcms/cms
        * core
          * domain：实体类
          * generator：生成功能（包括静态页生成，全文索引生成）
          * listener：监听类（包括数据删除的监听）
          * lucene：全文检索功能
          * mapper：MyBatis Mapper Java 文件
          * security：安全相关功能
          * service：服务层功能
          * support：各种支持类
          * web
            * api：前台API接口
            * backendapi：后台API接口
            * directive：前台Freemarker自定义标签
            * frontend：前台页面Controller
            * support：Web支持类（包括web拦截器等）
          * ContextConfig.java：Context配置类
          * LuceneConfig.java：全文索引配置类
          * ShiroConfig.java：Shiro安全配置类
          * TaskExecutorConfig.java：任务执行器配置类
        * Application.java 启动类
      * com/ujcms/util：公共工具类
    * resources
      * com/ujcms/cms/core/mapper：MyBatis Mapper XML 配置文件。
      * db
        * changelog：数据库表结构更新日志文件。
        * data.mysql.sql：数据库初始化数据SQL脚本。
      * application.yaml：程序配置文件。包括数据库URL、数据库用户名、数据库密码等信息。
      * messages.properties：国际化资源文件。
    * webapp：
      * cp：后台前端页面。
      * templates：网站前台模板。
      * uploads：用户上传文件。
      * WEB-INF/lucene：全文检索索引文件。
* .editorconfig：设置编辑器文件的格式，如缩进方式、最大行数等。
* .gitignore：设置不需要提交到git管理的文件和目录。
* CHANGELOG.md：版本更新日志。
* LICENSE：许可协议。
* gulpfile.js：前台构建文件。具有拷贝jquery、bootstrap等文件至前台模板目录`/src/main/webapp/template/1/default/_files`等功能。
* package.json：前台模板依赖的js、css组件，如jquery、bootstrap等。
* pom.xml：Maven配置文件。

