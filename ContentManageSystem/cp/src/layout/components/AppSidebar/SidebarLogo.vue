<script setup lang="ts">
defineProps({ collapse: { type: Boolean, required: true } });
const title = import.meta.env.VITE_APP_TITLE || '博物馆后台';
</script>

<template>
  <div class="flex h-12 items-center justify-center overflow-hidden">
    <router-link class="whitespace-nowrap text-center" to="/">
      <!-- 使用Font Awesome的博物馆图标 -->
      <i class="fa-solid fa-landmark text-primary" style="font-size: 24px;"></i>
      <h1 v-if="!collapse" class="inline align-middle ml-1 text-lg font-bold text-gray-100">{{ title }}</h1>
    </router-link>
  </div>
</template>
