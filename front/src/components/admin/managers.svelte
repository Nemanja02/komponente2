<script>
    import axios from 'axios';
    import { get } from 'svelte/store';
    import { user } from '../../lib/user.js';
    import { Table, TableBody, TableHead, TableHeadCell, TableBodyRow, TableBodyCell, Checkbox, Button, Modal, FloatingLabelInput, Label, Select } from 'flowbite-svelte';
    import { onMount } from 'svelte';
    let managers = [];
    let managerModal = false;
    let client = null;
    export const fetchData = () => {
        axios.get('http://localhost:8084/users/manager', {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            console.log(response.data);
            managers = response.data.content;
        }).catch((error) => {
            console.log(error);
        });
    }

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

    function editManager(id) {
        axios.get('http://localhost:8084/users/manager/' + id, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            client = response.data;
            managerModal = true;
        }).catch((error) => {
            console.log(error);
        })
    
    }

    function saveManager() {
        axios.put('http://localhost:8084/users/manager/' + client.id, {
            "id": client.id,
            "email": client.email,
            "firstName": client.firstName,
            "lastName": client.lastName,
            "username": client.username,
            "employmentDate": client.employmentDate,
            "gymId": client.gymId
        }, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            managerModal = false;
            fetchData();
        }).catch((error) => {
            console.log(error);
        })
    
    }
</script>

<div>
    <Table>
        <TableHead>
            <TableHeadCell>ID</TableHeadCell>
            <TableHeadCell>First name</TableHeadCell>
            <TableHeadCell>Last name</TableHeadCell>
            <TableHeadCell>Username</TableHeadCell>
            <TableHeadCell>Email</TableHeadCell>
            <TableHeadCell>Date of birth</TableHeadCell>
            <TableHeadCell>Verified</TableHeadCell>
            <TableHeadCell>Edit</TableHeadCell>
        </TableHead>
        <TableBody>
            {#each managers as client}
                <TableBodyRow>
                    <TableBodyCell>{client.id}</TableBodyCell>
                    <TableBodyCell>{client.firstName}</TableBodyCell>
                    <TableBodyCell>{client.lastName}</TableBodyCell>
                    <TableBodyCell>{client.username}</TableBodyCell>
                    <TableBodyCell>{client.email}</TableBodyCell>
                    <TableBodyCell>{client.employmentDate}</TableBodyCell>
                    <TableBodyCell>
                        <Checkbox checked={client.verified == 'verified'} disabled />
                    </TableBodyCell>
                    <TableBodyCell>
                        <Button color="primary" size="sm" outline on:click={editManager(client.id)}>Edit</Button>
                    </TableBodyCell>
                </TableBodyRow>
            {/each}
        </TableBody>
    </Table>
</div>

<Modal title="Edit Client" bind:open={managerModal}>
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
        <FloatingLabelInput bind:value={client.email} id="floating_standard" name="floating_standard" type="text" style="outlined" >
            Email
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={client.employmentDate} id="floating_standard" name="floating_standard" type="date" style="outlined">
            Employment date
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <Select bind:value={client.gymId} items={gyms.map(
            (val) => {
                return {value: val.id, name: val.name};
            })}></Select>
    </div>
    <Button class="w-full" on:click={saveManager}>Edit</Button>
</Modal>