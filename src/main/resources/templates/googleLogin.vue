<template>
    <div>
        <button @click="doGoogleLogin">구글 로그인</button>
    </div>
</template>
<script>
    export default {
        methods: {
            doGoogleLogin() {
                const url = "https://accounts.google.com/o/oauth2/v2/auth?client_id="
                    + process.env.VUE_APP_GOOGLE_CLIENT_ID
                    + "&redirect_uri="
                    + process.env.VUE_APP_GOOGLE_REDIRECT_URL
                    + "&response_type=code"
                    + "&scope=email profile";

                this.showSocialLoginPopup(url);
            },
            showSocialLoginPopup(url) {
                window.open(url, "_blank", "width=600, height=600");
            }
        }
    }
</script>