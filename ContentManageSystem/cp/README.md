# 后台管理子系统

使用 Vue 3、Vite、TypeScript、ElementPlus、TailwindCSS、VueRouter、VueI18n 开发。

需要启动WEB主项目才可以使用，不可单独运行（无法访问后端接口）。

## 搭建步骤

- 使用 vscode 开发工具。
- 安装 node 环境。Node 14.18+ / 16+ 版本。
- 使用淘宝 npm 镜像。
- 安装依赖，执行：npm install
- 启动程序，执行：npm run dev
- 访问：http://localhost:9520
- 用户名：admin，密码：password。

## 编译及部署

- 执行：npm run build
- 编译后的程序在`/dist`目录。
- 将`/dist`目录里的文件拷贝至主项目的`/src/main/webapp/cp`目录下（先将原目录下的文件删除）。

## 前后端分开部署

如果需要将前后端部署到不同域名或端口，如后端接口地址为`http://www.example.com/api`，前端地址为`http://www.frontend.com`。由于前后端域名不同，前端直接访问后端接口会出现跨域错误。这时需要在前端服务器部署反向代理，解决跨域问题。以`nginx`为例：

```
# 代理 api 接口
location /api {
    proxy_pass http://www.example.com;
}
# 代理上传文件
location /uploads {
    proxy_pass http://www.example.com;
}
```

开发模式启动时，情况也类似，后端接口地址为`http://localhost:8080/api`，前端地址为`http://localhost:9520`。前后端端口不同，也属于跨域。但前端开发在状态启动时，会自动开启代理，相关配置在`vite.config.ts`文件中。类似以下代码：

```
proxy: {
    '/api': {
        target: env.VITE_PROXY,
        changeOrigin: true,
    },
    '/uploads': {
        target: env.VITE_PROXY,
        changeOrigin: true,
    },
},
```

## 菜单和角色权限配置

如果进行二次开发，需新增功能，可在`/src/router/index.ts`文件中配置菜单。

并可在`/src/data.ts`文件中配置权限，配置好的权限会在`角色管理 - 权限设置`中的`功能权限`中显示。

配置内容：

```
export function getPermsTreeData(): any[] {
  const {
    global: { t },
  } = i18n;
  const perms = [
    ...
  ]
}
```
