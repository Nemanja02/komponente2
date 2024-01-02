<script>
  import Nav from "/src/components/nav.svelte";
  import {Card, FloatingLabelInput, Button, Spinner, Alert} from "flowbite-svelte";
  import axios from "axios";
  import {user} from "/src/lib/user.js";
  import {setContext, onMount} from "svelte";
  import {browser} from '$app/environment';

  let username = "nmarjanov6121rn@raf.rs";
  let password = "Test1234";
  let spinner = false;
  let alert = null;

  async function login() {
    spinner = true;
    axios.post("http://localhost:8084/users/user/login", {
      username: username,
      password: password,
    }).then((response) => {
      // decode if from jwt
      console.log(response);
      let base64Url = response.data.token.split(".")[1];
      let base64 = base64Url.replace("-", "+").replace("_", "/");
      let decodedToken = JSON.parse(window.atob(base64));
      user.set({
        token: response.data.token,
        id: decodedToken.id,
        role: decodedToken.role,
      });
      console.log(user);
      spinner = false;
      alert = {
        color: "green",
        message: "Successfully logged in!",
      };
    }).catch((error) => {
      spinner = false;
      try {
      alert = {
        color: "red",
        message: error.response.data.message || error.response.data.error_message,
      };
      } catch (e) {
      
      }
    });
  }

  $: {
      if (browser) {
        localStorage.setItem("user", JSON.stringify($user))
        if ($user.token) {
          window.location.href = "/";
        }
      }
  }
</script>
<Nav />
  <div class="mt-32"></div>
  {#if alert}
    <Alert border color={alert.color} class="ml-auto mr-auto w-1/5">
      {alert.message}
    </Alert>
  {/if}

  <Card class="ml-auto mr-auto bg-gray-50 text-center mt-8">


    <div class="font-size-16 text-2xl mb-6">Login</div>

    <div class="mb-6">
      <FloatingLabelInput id="floating_standard" name="floating_standard" type="text" style="outlined" bind:value={username}>
        Email
      </FloatingLabelInput>
    </div>

    <div class="mb-6">
      <FloatingLabelInput id="floating_standard" name="floating_standard" type="password" style="outlined" bind:value={password}>
        Password
      </FloatingLabelInput>
    </div>

    <Button class="w-full" on:click={login}>
      {#if spinner}
      <Spinner size={4} class="mr-2" /> 
      {/if}
      Login
    </Button>

  </Card>