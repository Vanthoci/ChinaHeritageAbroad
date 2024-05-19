<script lang="ts">
export default { name: 'BackupDatabaseList' };
</script>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { ElMessage, ElLoading } from 'element-plus';
import { Plus, Delete, Collection } from '@element-plus/icons-vue';
import { useI18n } from 'vue-i18n';
import dayjs from 'dayjs';
import { saveAs } from 'file-saver';
import { perm } from '@/stores/useCurrentUser';
import { pageSizes, pageLayout, toParams, resetParams } from '@/utils/common';
import { deleteBackupDatabase, queryBackupDatabaseList, downloadBackupDatabase, backupBackupDatabase, restoreBackupDatabase} from '@/api/file';
import { ColumnList, ColumnSetting } from '@/components/TableList';
import { QueryForm, QueryItem } from '@/components/QueryForm';

const { t } = useI18n();
const params = ref<any>({});
const sort = ref<any>();
const currentPage = ref<number>(1);
const pageSize = ref<number>(10);
const total = ref<number>(0);
const downloadLoading = ref<boolean>(false);
const table = ref<any>();
const data = ref<any[]>([]);
const selection = ref<any[]>([]);
const loading = ref<boolean>(false);
const formVisible = ref<boolean>(false);
const isBackingUp = ref<boolean>(false);
const beanId = ref<any>();
const beanIds = computed(() => data.value.map((row) => row.id));



const fetchData = async (showLoading?: boolean) => {
  if (showLoading == null || showLoading) {
    loading.value = true;
  }
  try {
    data.value = await queryBackupDatabaseList({ ...toParams(params.value), sort: sort.value });
    // filtered.value = Object.values(params.value).filter((v) => v !== undefined && v !== '').length > 0 || sort.value !== undefined;
  } finally {
    if (showLoading == null || showLoading) {
      loading.value = false;
    }
  }
};
onMounted(fetchData);

const handleSort = ({ column, prop, order }: { column: any; prop: string; order: string }) => {
  if (prop && order) {
    sort.value = (column.sortBy ?? prop) + (order === 'descending' ? '_desc' : '');
  } else {
    sort.value = undefined;
  }
  fetchData();
};
const handleSearch = () => fetchData();
const handleReset = () => {
  table.value.clearSort();
  resetParams(params.value);
  sort.value = undefined;
  fetchData();
};

const handleAdd = async () => {
  const messageInstance = ElMessage({
    message: '备份中... 请稍等',
    type: 'info',
    offset: 16,
    duration: 0,  
    showClose: false  
  });


  messageInstance;
  isBackingUp.value = true;
  // await new Promise(resolve => setTimeout(resolve, 3000));
  await backupBackupDatabase();
  isBackingUp.value = false;
  messageInstance.close();

  ElMessage.success('备份成功！');
  fetchData();
};

const handleRestore = async (name: string) => {
  // 显示全局加载提示，阻止用户操作
  const loading = ElLoading.service({
    lock: true,
    text: '正在恢复数据库，请稍候...',
    background: 'rgba(0, 0, 0, 0.7)'
  });

  isBackingUp.value = true;

  try {
    // 执行恢复数据库操作
    const response = await restoreBackupDatabase(name);
    console.log(response);
    ElMessage.success('数据库恢复成功！');
  } catch (error) {
    // 错误处理，显示错误信息
    console.error(error);
    ElMessage.error('数据库恢复失败！');
  } finally {
    // 操作完成，关闭加载提示
    loading.close();
    isBackingUp.value = false;
  }
};


const handleDownload = async (name: string) => {
  downloadLoading.value = true;
  try {
    const blob = await downloadBackupDatabase("/", [name]);
    saveAs(blob, name + '.zip');
  } finally {
    downloadLoading.value = false;
  }
  ElMessage.success('开始下载' + name);
  beanId.value = name;
  formVisible.value = true;
};

const handleDelete = async (ids: string[]) => {
  await deleteBackupDatabase(ids);
  fetchData();
  ElMessage.success('已删除');
};
</script>

<template>
  <div>
    <div>
      <el-popconfirm :title="$t('confirmBackup')" :width="170" @confirm="() => handleAdd()">{{ $t('backup') }}>
        <template #reference>
          <el-button type="primary" :disabled="isBackingUp"  :icon="Plus">{{ $t('backup') }}</el-button>
        </template>
      </el-popconfirm>
      <el-popconfirm :title="$t('confirmDelete')+'删除后无法还原!'" @confirm="() => handleDelete(selection.map((row) => row.id))">
        <template #reference>
          <el-button :disabled="selection.length <= 0 || perm('backupDatabase:delete')" :icon="Delete">{{ $t('delete')
            }}</el-button>
        </template>
      </el-popconfirm>
      <i class="el-icon-s-data"></i>
      <!-- <column-setting name="backupDatabase" class="ml-2" /> -->
    </div>
    <div class="mt-3 app-block">
      <el-table ref="table" v-loading="loading" :data="data" @selection-change="(rows) => (selection = rows)"
        @row-dblclick="(row) => handleDownload(row.origName)" @sort-change="handleSort">
        <column-list name="backupDatabase">
          <el-table-column type="selection" width="38"></el-table-column>
          <!-- <el-table-column property="id" label="ID" width="80" sortable="custom"></el-table-column> -->

          <el-table-column label="名称" min-width="120" sortable="custom">
            <template #default="scope">
              <!-- 这里添加图标和文本 -->
              <el-icon style="color: #0099ff;">
                <collection /> <!-- 这里的 <collection /> 就是图标组件 -->
              </el-icon>
              <span style="color: #0099ff;">&NonBreakingSpace;{{ scope.row.name }}</span>
            </template>
          </el-table-column>

          <el-table-column property="date" :label="'创建日期'" min-width="120" sortable="custom" display="none"
            show-overflow-tooltip>
            <template #default="{ row }">{{ dayjs(row.lastModified).format('YYYY-MM-DD HH:mm:ss') }}</template>
          </el-table-column>

          <el-table-column property="size" :label="'大小'" min-width="80" sortable="custom"
            show-overflow-tooltip></el-table-column>


          <el-table-column :label="$t('table.action')">
            <template #default="{ row }">

              <el-popconfirm :title="$t('backupTemplates.confirm.restore')" :width="290"
                @confirm="() => handleRestore(row.origName)">
                <template #reference>
                  <el-button type="primary" :disabled="perm('backupDatabase:delete')" size="small" link>{{
                    $t('backupDatabase.op.restore') }}</el-button>
                </template>
              </el-popconfirm>

              <el-popconfirm :title="$t('confirmDelete')" @confirm="() => handleDelete([row.id])">
                <template #reference>
                  <el-button type="primary" :disabled="perm('backupDatabase:delete')" size="small" link>{{ $t('delete')
                    }}</el-button>
                </template>
              </el-popconfirm>

            </template>
          </el-table-column>
        </column-list>

      </el-table>
      <!-- <el-pagination v-model:current-page="currentPage" v-model:pageSize="pageSize" :total="total"
        :page-sizes="pageSizes" :layout="pageLayout" class="justify-end px-3 py-2" small background
        @size-change="() => fetchData()" @current-change="() => fetchData()"></el-pagination> -->
    </div>
  </div>
</template>
