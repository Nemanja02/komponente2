<script>
    import Nav from "../components/nav.svelte";
    import {user} from "../lib/user.js";
    import {get} from "svelte/store";
    import AdminHome from "../components/admin_home.svelte";
    import ClientHome from "../components/client_home.svelte";
    import ManagerHome from "../components/manager_home.svelte";

    // get token 

    function getRole() {
        console.log(get(user));
        if (!get(user).token) return;
        let token = get(user).token;
        let base64Url = token.split(".")[1];
        let base64 = base64Url.replace("-", "+").replace("_", "/");
        let decodedToken = JSON.parse(window.atob(base64));
        return decodedToken.role;
    }
</script>

<Nav />
{#if getRole() == "ROLE_ADMIN"}
    <AdminHome />
{/if}
{#if getRole() == "ROLE_USER"}
    <ClientHome />
{/if}
{#if getRole() == "ROLE_MANAGER"}
    <ManagerHome />
{/if}

