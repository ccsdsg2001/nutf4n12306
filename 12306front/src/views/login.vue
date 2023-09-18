<template>
  <div class="login" style="background-image: url('https://nutf4n.oss-cn-guangzhou.aliyuncs.com/at4r7x6utpey0u2wpdnz3r6estrg7usr.jpg');">
    <div class="login-main">
      <h1><a-icon type="rocket-two-tone" /> 12306NutF4n</h1>
      <a-form :model="loginForm" name="basic" autocomplete="off">
        <a-form-item label="" name="mobile" :rules="[{ required: true, message: '请输入手机号!' }, { validator: validateMobile }]">
          <a-input v-model:value="loginForm.mobile" placeholder="手机号" />
        </a-form-item>

        <a-form-item label="" name="code" :rules="[{ required: true, message: '请输入验证码!' }]">
          <a-input v-model:value="loginForm.code">
            <template #addonAfter>
              <a-button @click="sendCode" :disabled="countdown > 0">{{ countdown > 0 ? countdown + 's后再试' : '获取验证码' }}</a-button>
            </template>
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" block @click="login">登录</a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script>
import { defineComponent, reactive, ref } from 'vue';
import axios from 'axios';
import { notification } from 'ant-design-vue';
import { useRouter } from 'vue-router'
import store from "@/store";

export default defineComponent({
  name: "login-view",
  setup() {
    const router = useRouter();

    const loginForm = reactive({
      mobile: '',
      code: '',
    });

    const countdown = ref(0);

    const validateMobile = (_, value) => {
      const reg = /^1[3456789]\d{9}$/;
      if (!reg.test(value)) {
        return Promise.reject('请输入正确的手机号码!');
      }
      return Promise.resolve();
    };

    const sendCode = () => {
      if (countdown.value > 0) {
        return;
      }

      axios.post("/member/member/send-code", {
        mobile: loginForm.mobile
      }).then(response => {
        let data = response.data;
        if (data.success) {
          notification.success({ description: '发送验证码成功！' });
          countdown.value = 60;
          startCountdown();
        } else {
          notification.error({ description: data.message });
        }
      });
    };

    const startCountdown = () => {
      if (countdown.value > 0) {
        setTimeout(() => {
          countdown.value--;
          startCountdown();
        }, 1000);
      }
    };

    const login = () => {
      axios.post("/member/member/login", loginForm).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({ description: '登录成功！' });
          router.push("/welcome");
          store.commit("setMember", data.content);
        } else {
          notification.error({ description: data.message });
        }
      })
    };

    return {
      loginForm,
      countdown,
      sendCode,
      login,
      validateMobile
    };
  },
});
</script>


<style scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-size: cover;
  background-repeat: no-repeat;
}

.login-main {
  width: 400px;
  padding: 30px;
  border: 2px solid grey;
  border-radius: 10px;
  background-color: #fcfcfc;
}

.login-main h1 {
  font-size: 25px;
  font-weight: bold;
  margin-bottom: 20px;
  text-align: center;
}

.login-main a-icon {
  font-size: 30px;
  margin-right: 10px;
}

.login-main a-button {
  width: 100%;
}
</style>