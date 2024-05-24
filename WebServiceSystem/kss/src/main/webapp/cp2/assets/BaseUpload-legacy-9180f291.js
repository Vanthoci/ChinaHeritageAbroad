System.register(["./index-legacy-414c7520.js","./config-legacy-94002286.js","./BaseUpload.vue_vue_type_style_index_0_scoped_bf818630_lang-legacy-30235b0a.js"],(function(e,a){"use strict";var t,l,u,r,i,n,o,c,s,d,p,f,y,v,g,m,b,_,B,S,A,h,w,I,x,L,j;return{setters:[e=>{t=e.d,l=e.p,u=e.u,r=e.a,i=e.r,n=e.a6,o=e.b,c=e.e,s=e.I,d=e.i,p=e.w,f=e.b1,y=e.j,v=e.ac,g=e.ad,m=e.f,b=e.h,_=e.E,B=e.af,S=e.k,A=e.t,h=e._},e=>{w=e.f,I=e.Q,x=e.R,L=e.S,j=e.i},null],execute:function(){const a=t({__name:"BaseUpload",props:{type:{type:String,default:"file",validator:e=>["image","video","audio","library","doc","file","any"].includes(e)},button:{type:String,default:null},data:{type:Object,default:null},uploadAction:{type:String,default:null},fileAccept:{type:String,default:null},fileMaxSize:{type:Number,default:null},multiple:{type:Boolean},disabled:{type:Boolean,default:!1},onSuccess:{type:Function,default:null}},setup(e){const a=e,{type:t,uploadAction:h,fileAccept:z,fileMaxSize:M}=l(a),{t:$}=u(),k=r(),E=i({}),O=n((()=>{if(null!=h?.value)return h.value;switch(t.value){case"image":return j;case"video":return L;case"audio":return x;case"library":case"doc":return I;case"file":return w;default:throw new Error(`Type not support: ${t.value}`)}})),T=n((()=>{if(null!=z?.value)return z.value;switch(t.value){case"image":return k.upload.imageInputAccept;case"video":return k.upload.videoInputAccept;case"audio":return k.upload.audioInputAccept;case"library":return k.upload.libraryInputAccept;case"doc":return k.upload.docInputAccept;case"file":return k.upload.fileInputAccept;case"any":return;default:throw new Error(`Type not support: ${t.value}`)}})),U=n((()=>{if(null!=M?.value)return M.value;switch(t.value){case"image":return k.upload.imageLimitByte;case"video":return k.upload.videoLimitByte;case"audio":return k.upload.audioLimitByte;case"library":return k.upload.libraryLimitByte;case"doc":return k.upload.docLimitByte;case"file":return k.upload.fileLimitByte;default:return 0}})),N=e=>!(U.value>0&&e.size>U.value&&(_.error($("error.fileMaxSize",{size:U.value/1024/1024+" MB"})),1)),R=e=>{B(JSON.parse(e.message))};return(a,t)=>{const l=o("el-upload"),u=o("el-progress");return c(),s("div",null,[d(l,{action:O.value,headers:{...y(v)(),...y(g)()},data:e.data,accept:T.value,"before-upload":N,"on-success":e.onSuccess,"on-progress":(e,a)=>E.value=a,"on-error":R,"show-file-list":!1,disabled:e.disabled,multiple:e.multiple,drag:""},{default:p((()=>[f(a.$slots,"default",{},(()=>[S("span",null,A(e.button??a.$t("clickOrDragToUpload")),1)]),!0)])),_:3},8,["action","headers","data","accept","on-success","on-progress","disabled","multiple"]),"uploading"===E.value.status?(c(),m(u,{key:0,percentage:parseInt(E.value.percentage,10)},null,8,["percentage"])):b("",!0)])}}});e("B",h(a,[["__scopeId","data-v-bf818630"]]))}}}));