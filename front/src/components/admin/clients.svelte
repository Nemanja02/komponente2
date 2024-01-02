<script>
    import {Table, TableHead, TableHeadCell, TableBody, TableBodyRow, TableBodyCell, Button, Checkbox, Modal, FloatingLabelInput, Toggle} from "flowbite-svelte";
    import axios from "axios";
    import {get} from "svelte/store";
    import {user} from "../../lib/user.js";
    let clients = [];
    let clientModal = false;
    let client = {
        "id": '',
        "email": "",
        "firstName": "",
        "lastName": "",
        "username": "",
        "dateOfBirth": "",
    }

    export const fetchData = () => {
        axios.get("http://localhost:8084/users/client", {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            clients = response.data.content;
        }).catch((error) => {
            console.log(error);
        });
    }

    function editClient(id) {
        axios.get('http://localhost:8084/users/client/' + id, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            client = response.data;
            clientModal = true;
        }).catch((error) => {
            console.log(error);
        })
    }

    function saveClient() {
        axios.put('http://localhost:8084/users/client/' + client.id, {
            "id": client.id,
            "email": client.email,
            "firstName": client.firstName,
            "lastName": client.lastName,
            "username": client.username,
            "dateOfBirth": client.dateOfBirth
        }, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            clientModal = false;
            fetchData();
        }).catch((error) => {
            console.log(error);
        })
    
    }

    function enableClient(id) {
        let checked = event.target.checked;
        console.log(checked);
        axios.post(`http://localhost:8084/users/admin/${checked ? 'enable' : 'disable'}/${id}` , {}, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            fetchData();
        }).catch((error) => {
            console.log(error);
        })
    }
</script>


<div>
    <Table>
        <TableHead>
            <TableHeadCell>Membership number</TableHeadCell>
            <TableHeadCell>First name</TableHeadCell>
            <TableHeadCell>Last name</TableHeadCell>
            <TableHeadCell>Username</TableHeadCell>
            <TableHeadCell>Email</TableHeadCell>
            <TableHeadCell>Date of birth</TableHeadCell>
            <TableHeadCell>Verified</TableHeadCell>
            <TableHeadCell>Enabled</TableHeadCell>
            <TableHeadCell>Training Count</TableHeadCell>
            <TableHeadCell>Edit</TableHeadCell>
        </TableHead>
        <TableBody>
            {#each clients as client}
                <TableBodyRow>
                    <TableBodyCell>{client.id}</TableBodyCell>
                    <TableBodyCell>{client.firstName}</TableBodyCell>
                    <TableBodyCell>{client.lastName}</TableBodyCell>
                    <TableBodyCell>{client.username}</TableBodyCell>
                    <TableBodyCell>{client.email}</TableBodyCell>
                    <TableBodyCell>{client.dateOfBirth}</TableBodyCell>
                    <TableBodyCell>
                        <Checkbox checked={client.verified == 'verified'} disabled />
                    </TableBodyCell>
                    <TableBodyCell>
                        <Toggle checked={client.active} on:change={enableClient(client.id)} />
                    </TableBodyCell>
                    <TableBodyCell>{client.trainingCount}</TableBodyCell>
                    <TableBodyCell>
                        <Button color="primary" size="sm" outline on:click={editClient(client.id)}>Edit</Button>
                    </TableBodyCell>
                </TableBodyRow>
            {/each}
        </TableBody>
    </Table>
</div>

<Modal title="Edit Client" bind:open={clientModal}>
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
        <FloatingLabelInput bind:value={client.dateOfBirth} id="floating_standard" name="floating_standard" type="date" style="outlined">
            Date of birth
        </FloatingLabelInput>
    </div>
    <Button class="w-full" on:click={saveClient}>Edit</Button>
</Modal>