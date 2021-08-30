<template>
  <div class="app-container">
    <p> File uploader by Vue-simlple-uploader </p>
    <uploader
      class="uploader-example"
      :options="options"
      :auto-start="false"
      @file-success="onFileSuccess"
    >
      <uploader-unsupport />
      <uploader-drop>
        <p>Drop files here to upload or </p>
        <uploader-btn>select files</uploader-btn>
      </uploader-drop>
      <uploader-list />
    </uploader>
  </div>
</template>

<script>
import Vue from 'vue'
import uploader from 'vue-simple-uploader'
import { Message } from 'element-ui'

Vue.use(uploader)

export default {

  data() {
    return {
      options: {
        target: process.env.VUE_APP_BASE_API + '/api/fileupload',
        testChunks: true, // 秒传
        chunkSize: 10 * 1024 * 1024,
        forceChunkSize: true, // 强制chunk小于chunkSize 否则可能是chunkSize - 2*chunkSize之间的大小
        simultaneousUploads: 8, // 并发接口数
        query: {
          destinationPath: 'D:\\FileReveiver'
        }, // 额外传递的form表单中参数
        method: 'multipart', // 表单类型
        uploadMethod: 'POST', // 上传用的HTTP接口方法
        maxChunkRetries: 3, // 每个切片post的最大重试次数
        successStatuses: [200, 201, 202], // 接口成功的状态码
        permanentErrors: [404, 415, 500, 501], // 接口失败的状态码
        checkChunkUploadedByResponse: function(chunk, message) {
          /**
           * 处理每个切片的post之前的get, 重写后则只会发一个get.
           * 每个切片的post之前就调用这个方法,chunk改变为每个切片 message不变，为第一次post的返回体response
           * 如果返回true 则该切片不post; 返回false 则post该切片.
           */
          console.log('checkChunkUploadedByResponse, chunk: %o', chunk)
          var messageObj = JSON.parse(message)
          console.log('checkChunkUploadedByResponse, message: %o', messageObj)
          if (messageObj.skipUpload) {
            console.log('Skip post, file transfer in seconds')
            return true
          }
          var existChunks = messageObj.existChunks
          var isCurrentChunkExist = (existChunks || []).indexOf(chunk.offset + 1) >= 0
          return isCurrentChunkExist
        }
      }
    }
  },
  watch: {
  },
  methods: {
    onFileSuccess() {
      Message.success('success')
    }
  }
}
</script>

<style>
  .uploader-example {
    width: 880px;
    padding: 15px;
    margin: 40px auto 0;
    font-size: 12px;
    box-shadow: 0 0 10px rgba(0, 0, 0, .4);
  }
  .uploader-example .uploader-btn {
    margin-right: 4px;
  }
  .uploader-example .uploader-list {
    max-height: 440px;
    overflow: auto;
    overflow-x: hidden;
    overflow-y: auto;
  }
</style>

