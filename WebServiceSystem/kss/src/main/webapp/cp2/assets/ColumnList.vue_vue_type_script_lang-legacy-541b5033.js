System.register(["./index-legacy-414c7520.js"],(function(e,t){"use strict";var n,s,l,r,i,o;return{setters:[e=>{n=e.b7,s=e.b8,l=e.d,r=e.p,i=e.u,o=e.a6}],execute:function(){e("s",(function(){localStorage.setItem(t,JSON.stringify(a))}));const t="ujcms_column_settings",u=n({}),a=n(function(){const e=localStorage.getItem(t);return e?JSON.parse(e):{}}());e("a",(e=>(u[e]||(u[e]=[]),s(u,e))));const c=e("m",((e,t)=>{for(let n=0,s=e.length;n<s;)-1===t.findIndex((t=>t.title===e[n].title))?(e.splice(n,1),s-=1):n+=1;return t.forEach((t=>{-1===e.findIndex((e=>e.title===t.title))&&e.push({...t})})),e})),p=e("g",(e=>(a[e]||(a[e]=[]),s(a,e))));e("_",l({name:"ColumnList",props:{name:{type:String,required:!0}},setup(e,{slots:t}){const{name:n}=r(e),{t:s}=i(),l=t.default?.()??[],d=e=>"selection"===e?.type?s("table.selection"):e?.label,f=l.map((e=>({title:d(e.props),display:"none"!==e.props?.display})));((e,t)=>{u[e]=t,a[e]||(a[e]=[]);const n=a[e];c(n,t)})(n.value,f);const m=p(n.value);return{columns:o((()=>l.filter((e=>{const t=m.value.find((t=>d(e.props)===t.title));return!t||t.display})).map((e=>({...e,key:d(e.props)}))).sort(((e,t)=>{let n=m.value.findIndex((t=>t.title===d(e)));n<0&&(n=l.findIndex((t=>d(t)===d(e))));let s=m.value.findIndex((e=>e.title===d(t)));return s<0&&(s=l.findIndex((e=>d(e)===d(t)))),n-s}))))}},render(){return this.columns}}))}}}));