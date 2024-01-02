<script>
    import {Card, FloatingLabelInput, Button, Tabs, TabItem} from 'flowbite-svelte';
    import axios from "axios";
    import {get} from "svelte/store";
    import {user} from "../lib/user.js";
    import {onMount} from "svelte";
    import Booking from './booking.svelte';

    import UserNotifications from './admin/user_notifications.svelte';

    let client = {
        "id": '',
        "email": "",
        "firstName": "",
        "lastName": "",
        "username": "",
        "dateOfBirth": "",
        "userType": "",
        "password": "",
        "membershipNumber": null,
        "gymName": null,
        "employmentDate": null,
        "verified": null
    }

    onMount(async () => {
        let token = get(user).token;
        let base64Url = token.split(".")[1];
        let base64 = base64Url.replace("-", "+").replace("_", "/");
        let decodedToken = JSON.parse(window.atob(base64));
        let id = decodedToken.id;

        axios.get("http://localhost:8084/users/client/" + id).then((response) => {
            console.log(response);
            client = response.data;
        }).catch((error) => {
            console.log(error);
        });
    });

    function edit() {
        let datum = new Date(client.dateOfBirth);
        let year = datum.getFullYear();
        let month = datum.getMonth() + 1;
        if (month < 10) month = "0" + month;
        let day = datum.getDate();
        if (day < 10) day = "0" + day;
        let data = {
                "id": client.id,
                "email": client.email,
                "firstName": client.firstName,
                "lastName": client.lastName,
                "username": client.username,
                "dateOfBirth": year + "-" + month + "-" + day,
            }
        if (client.password != "") {
            data.password = client.password;
        }
        axios.put("http://localhost:8084/users/client/" + client.id, data, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
        }).catch((error) => {
            console.log(error);
        });
    
    }

    let fetchNotifications;

    
</script>
<div class="flex ml-auto mr-auto justify-items-center pr-8 pl-8" style="justify-content:center">
    <Card class="mr-8 mt-8 bg-gray-50 text-center w-1/5" size="md">
        <div class="font-size-16 text-2xl mb-6">Profile</div>

        <div class="mb-6">
            <FloatingLabelInput bind:value={client.id} id="floating_standard" name="floating_standard" type="text" style="outlined" disabled>
                Membership number
            </FloatingLabelInput>
        </div>
        <div class="mb-6">
            <FloatingLabelInput bind:value={client.firstName} id="floating_standard" name="floating_standard" type="text" style="outlined" >
                First name
            </FloatingLabelInput>
        </div>
            <div class="mb-6">
            <FloatingLabelInput bind:value={client.lastName} id="floating_standard" name="floating_standard" type="text" style="outlined" >
                Last name
            </FloatingLabelInput>
        </div>
            <div class="mb-6">
            <FloatingLabelInput bind:value={client.username} id="floating_standard" name="floating_standard" type="text" style="outlined" >
                Username
            </FloatingLabelInput>
        </div>
        <div class="mb-6">
            <FloatingLabelInput bind:value={client.password} id="floating_standard" name="floating_standard" type="password" style="outlined" >
                Password
            </FloatingLabelInput>
        </div>
        <div class="mb-6">
            <FloatingLabelInput bind:value={client.email} id="floating_standard" name="floating_standard" type="text" style="outlined" >
                Email
            </FloatingLabelInput>
        </div>
            <div class="mb-6">
            <FloatingLabelInput bind:value={client.dateOfBirth} id="floating_standard" name="floating_standard" type="date" style="outlined">
                Date of birth
            </FloatingLabelInput>
        </div>

        <div class="">
            <Button color="primary" class="ml-auto mr-auto w-full" on:click={edit}>Edit</Button>
        </div>
    </Card>
    <Card class=" mt-8 bg-gray-50 text-center w-4/5" size="xxl" padding="none">
        <Tabs style="underline">
            <TabItem open title="Booking">
                <Booking />
            </TabItem>
            <TabItem title="Notifications" on:click={fetchNotifications}>
                <UserNotifications bind:fetchData={fetchNotifications} />
            </TabItem>
        </Tabs>
    </Card>
</div>