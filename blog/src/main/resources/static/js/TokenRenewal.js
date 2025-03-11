async function getOAuth2Token(authCode) {
    try {
        const response = await axios.post("http://localhost:8084/oauth2/token", {
            code: authCode,
            grant_type: "authorization_code",
            client_id: "1090860626822-3elf6jm8vtbivfap7778jbqf0lmv97a9.apps.googleusercontent.com",
            client_secret: "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",
            redirect_uri: "http://localhost:8084/oauth/callback"
        });

        const { access_token, refresh_token } = response.data;

        // 🔥 받은 Access Token을 저장 (로컬 스토리지 or 세션 스토리지)
        localStorage.setItem("access_token", access_token);
        if (refresh_token) {
            localStorage.setItem("refresh_token", refresh_token);
        }

        console.log("Access Token:", access_token);
        return access_token;
    } catch (error) {
        console.error("토큰 요청 실패:", error);
        alert("OAuth2 토큰 요청 실패!");
    }
}

window.onload = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const authCode = urlParams.get("code");

    if (authCode) {
        const token = await getOAuth2Token(authCode);
        console.log("토큰 받음:", token);
    }
}