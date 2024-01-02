<script>
    import Nav from "/src/components/nav.svelte";
    import {Card, FloatingLabelInput, Button, Alert} from "flowbite-svelte";
    import axios from "axios";
  
    let username = "Nemanja02";
    let password = "Test1234";
    let firstName = "Nemanja";
    let lastName = "Marjanov";
    let email = "nmarjanov6121rn@raf.rs";
    let dateOfBirth = "25.01.2002";
    let alert = null;
  
    function register() {
  
      // format date yyyy-mm-dd
      // month and day must be 2 digits
      let datum = new Date(dateOfBirth);
      let year = datum.getFullYear();
      let month = datum.getMonth() + 1;
      if (month < 10) month = "0" + month;
      let day = datum.getDate();
      if (day < 10) day = "0" + day;
      dateOfBirth = year + "-" + month + "-" + day;
      axios.post("http://localhost:8084/users/client/register", {
        username: username,
        password: password,
        firstName: firstName,
        lastName: lastName,
        email: email,
        dateOfBirth: dateOfBirth,
        userType: "client"
      }).then((response) => {
        console.log(response);
        alert = {
          color: "green",
          message: "Successfully registered!",
        };
      }).catch((error) => {
        alert = {
          color: "red",
          message: error.response.data.message,
        };
        alert(error.response.data.message);
      });
    }
  </script>
  <Nav />
  
    <div class="mt-32"></div>
    {#if alert}
      <Alert color={alert.color} class="ml-auto mr-auto w-1/5">
        {alert.message}
      </Alert>
    {/if}
  
    <Card class="ml-auto mr-auto mt-8 bg-gray-50 text-center">
      <div class="font-size-16 text-2xl mb-6">Register</div>
  
      <div class="mb-6">
        <FloatingLabelInput id="floating_standard" name="floating_standard" type="text" style="outlined" bind:value={username}>
          Username
        </FloatingLabelInput>
      </div>
  
      <div class="mb-6">
        <FloatingLabelInput id="floating_standard" name="floating_standard" type="password" style="outlined" bind:value={password}>
          Password
        </FloatingLabelInput>
      </div>
  
      <div class="mb-6">
        <FloatingLabelInput id="floating_standard" name="floating_standard" type="text" style="outlined" bind:value={firstName}>
          First name
        </FloatingLabelInput>
      </div>
  
      <div class="mb-6">
        <FloatingLabelInput id="floating_standard" name="floating_standard" type="text" style="outlined" bind:value={lastName}>
          Last name
        </FloatingLabelInput>
      </div>
  
      <div class="mb-6">
        <FloatingLabelInput id="floating_standard" name="floating_standard" type="email" style="outlined" bind:value={email}>
          Email
        </FloatingLabelInput>
      </div>
  
      <div class="mb-6">
        <FloatingLabelInput id="floating_standard" name="floating_standard" type="date" style="outlined" bind:value={dateOfBirth}>
          Date of birth
        </FloatingLabelInput>
      </div>
  
      <Button class="w-full" on:click={register}>Register</Button>
  
    </Card>