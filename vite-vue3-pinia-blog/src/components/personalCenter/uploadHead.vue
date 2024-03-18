<template>
    <span>上传一个新的头像吧,若头像加载失败请刷新页面</span>
    <el-upload class="avatar-uploader" method="post" :action="pinia.apiRoot + '/api/uploadhead'" show-file-list
        :before-upload="beforeAvatarUpload" :auto-upload="false" list-type="picture" ref="uploadRef" :limit="1"
        :http-request="elUploadFunc">
        <el-icon class="avatar-uploader-icon">
            <Plus />
        </el-icon>
    </el-upload>
</template>
  
<script lang="ts" setup>
import { ref, watchEffect } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { UploadProps, UploadInstance } from 'element-plus'
import { useStore } from "@/store/count";
import { useRouter } from 'vue-router';
import useAxios from '@/hooks/axios/axios';

const pinia = useStore()
const router = useRouter()
const emit = defineEmits(['uploadHeadSuccess'])

type Props = {
    ifsendHead: boolean
}
const props = defineProps<Props>()

//获取上传的表单元素dom
const uploadRef = ref<UploadInstance>()
//上传头
//获取当前登录的账号
const session = JSON.parse(window.atob(localStorage.getItem('userAccount')!))

watchEffect(() => {
    if (props.ifsendHead === true) {
        uploadRef.value!.submit()
    }
})

// 上传函数
const elUploadFunc = async (doParam: any) => {
    const formHead = new FormData();
    formHead.append("account", session.account)
    formHead.append("file", doParam.file)
    useAxios.post("/uploadhead", formHead).then((res) => {
        if (res.data.status === 0) {
            alert("头像更换成功")
            router.go(0)
        } else {
            alert("发生错误")
        }
    })
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
    if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error('大小限制为2M!')
        return false
    }
    return true
}
</script>
  
<style scoped>
.avatar-uploader .avatar {
    width: 178px;
    height: 178px;
    display: block;
}
</style>
  
<style>
.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
}
</style>
  