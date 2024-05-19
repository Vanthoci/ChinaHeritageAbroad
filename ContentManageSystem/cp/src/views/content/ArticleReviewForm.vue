<script lang="ts">
export default { name: 'CommentsForm' };
</script>

<script setup lang="ts">
import { onMounted, ref, PropType } from 'vue';
import { queryComments, createComments, updateComments, deleteComments } from '@/api/interaction';
import { queryDictListByAlias } from '@/api/content';
import Tinymce from '@/components/Tinymce';
import DialogForm from '@/components/DialogForm.vue';
import LabelTip from '@/components/LabelTip.vue';



defineProps({ modelValue: { type: Boolean, required: true }, beanId: { type: Number, default: null }, beanIds: { type: Array as PropType<number[]>, required: true } });
defineEmits({ 'update:modelValue': null, finished: null });
const focus = ref<any>();
const values = ref<any>({});
const commentsTypeList = ref<any[]>([]);

const fetchCategoryList = async () => {
//   commentsTypeList.value = await queryDictListByAlias('sys_comments_type');
};


const statusOptions = [
  { label: '已过审', value: 0 },
  { label: '待审核', value: 1 },
  { label: '已屏蔽', value: 2 }
];

onMounted(async () => {
  fetchCategoryList();
});
</script>

<template>
  <dialog-form
    v-model:values="values"
    :name="$t('menu.interaction.comments')"
    :query-bean="queryComments"
    :create-bean="createComments"
    :update-bean="updateComments"
    :delete-bean="deleteComments"
    :bean-id="beanId"
    :bean-ids="beanIds"
    :focus="focus"
    :addable="false"
    :init-values="() => ({ typeId: commentsTypeList[0]?.id, userType: 1, open: true, status: 0 })"
    :to-values="(bean) => ({ ...bean })"
    perms="comments"
    :model-value="modelValue"
    large
    @update:model-value="(event) => $emit('update:modelValue', event)"
    @finished="() => $emit('finished')"
  >
    <template #header-status="{ isEdit }">
      <template v-if="isEdit">
        <span v-if="values.status != null">
          <el-tag v-if="values.status === 0" type="success" size="small" disable-transitions>{{ $t(`comments.status.${values.status}`) }}</el-tag>
          <el-tag v-else-if="values.status === 1" type="info" size="small" disable-transitions>{{ $t(`comments.status.${values.status}`) }}</el-tag>
          <el-tag v-else-if="values.status === 2" type="danger" size="small" disable-transitions>{{ $t(`comments.status.${values.status}`) }}</el-tag>
          <el-tag v-else type="info" size="small" disable-transitions>unknown</el-tag>
        </span>
      </template>
    </template>
    <template #default="{ isEdit }">
      <el-row>
        <el-col :span="24">
            <el-form-item prop="status" :rules="{ required: true, message: () => $t('v.required') }">
            <template #label><label-tip message="comments.status" /></template>
            <el-select v-model="values.status">
                <el-option v-for="option in statusOptions" :key="option.value" :label="option.label" :value="option.value" />
            </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="username">
            <template #label><label-tip message="comments.user" /></template>
            <el-input :model-value="values.username" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item prop="text" :rules="{ required: false, message: () => $t('v.required') }">
            <template #label><label-tip message="comments.text" /></template>
            <el-input v-model="values.text" type="textarea" rows="4" maxlength="255" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="ip">
            <template #label><label-tip message="comments.ip" /></template>
            <el-input :model-value="values.ip" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="ip">
            <template #label><label-tip message="comments.article" /></template>
            <el-input :model-value="values.articleId" disabled></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="created">
            <template #label><label-tip message="comments.created" /></template>
            <el-date-picker :model-value="values.created" type="datetime" class="w-full" disabled></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
  </dialog-form>
</template>
