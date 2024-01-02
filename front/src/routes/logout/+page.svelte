<script>
    import { setContext } from "svelte";
    import { user } from "../../lib/user.js";
    import { browser } from '$app/environment';

    setContext("user", { token: null, id: null });
    
    console.log(1)
    if (browser) {
        console.log(2)
        localStorage.setItem("user", JSON.stringify({ token: null, id: null }));

        window.location.href = "/login";
    }
</script>