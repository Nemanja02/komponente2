<script>
  import Nav from "/src/components/nav.svelte";
  import {Card, FloatingLabelInput, Button, Alert, Select, Label} from "flowbite-svelte";
  import axios from "axios";
    import { onMount } from "svelte";

  let username = "Nemanja02";
  let password = "Test1234";
  let firstName = "Nemanja";
  let lastName = "Marjanov";
  let email = "nmarjanov6121rn@raf.rs";
  let dateOfBirth = "25.01.2002";
  let employmentDate = "25.01.2002";
  let gymId = "1";
  let alert = null;

  let gyms = [];

  function fetchGyms() {
    axios.get("http://localhost:8084/trainings/gym").then((response) => {
      console.log(response);
      gyms = response.data;
    }).catch((error) => {
      console.log(error);
    });
  }

  onMount(async () => {
    fetchGyms();
  });

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

    datum = new Date(employmentDate);
    year = datum.getFullYear();
    month = datum.getMonth() + 1;
    if (month < 10) month = "0" + month;
    day = datum.getDate();
    if (day < 10) day = "0" + day;
    employmentDate = year + "-" + month + "-" + day;
    axios.post("http://localhost:8084/users/manager/register", {
      username: username,
      password: password,
      firstName: firstName,
      lastName: lastName,
      email: email,
      dateOfBirth: dateOfBirth,
      employmentDate: employmentDate,
      gymId: gymId,
      userType: "manager"
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

  <div class="mt-16"></div>
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

    <div class="mb-6">
      <FloatingLabelInput id="floating_standard" name="floating_standard" type="date" style="outlined" bind:value={employmentDate}>
        Employment date
      </FloatingLabelInput>
    </div>

    <div class="mb-6 text-left">
      <Label class="mb-2">Gym</Label>
      <Select bind:value={gymId} items={gyms.map(
        (val) => {
          return {value: val.id, name: val.name};
        })}></Select>
    </div>

    <Button class="w-full" on:click={register}>Register</Button>

  </Card>