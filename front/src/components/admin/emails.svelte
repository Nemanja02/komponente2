<script>
    import { Table, TableBody, TableBodyCell, TableHead, TableHeadCell, TableBodyRow, Button,FloatingLabelInput,Modal,Textarea,Label } from "flowbite-svelte";
    import axios from "axios";
    import { get } from "svelte/store";
    import { user } from "../../lib/user.js";
    let emails = []
    let emailModal = false;

    let email = {
        "id": '',
        "subject": '',
        "body": '',
        "type": ''
    }

    export const fetchData = () => {
        axios.get("http://localhost:8084/users/email", {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            emails = response.data.content;
        }).catch((error) => {
            console.log(error);
        });
    }

    function editEmail(id) {
        axios.get('http://localhost:8084/users/email/' + id, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            email = response.data;
            emailModal = true;
        }).catch((error) => {
            console.log(error);
        })
        
    }

    function saveEmail() {
        axios.put('http://localhost:8084/users/email/' + email.id, {
            "id": email.id,
            "subject": email.subject,
            "body": email.body,
            "type": email.type
        }, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            emailModal = false;
            fetchData();
        }).catch((error) => {
            console.log(error);
        })
    }
</script>

<div>
    <Table>
        <TableHead>
            <TableHeadCell>Id</TableHeadCell>
            <TableHeadCell>Subject</TableHeadCell>
            <TableHeadCell>Content</TableHeadCell>
            <TableHeadCell>Type</TableHeadCell>
            <TableHeadCell>Edit</TableHeadCell>
        </TableHead>
        <TableBody>
            {#each emails as email}
                <TableBodyRow>
                    <TableBodyCell>{email.id}</TableBodyCell>
                    <TableBodyCell>{email.subject}</TableBodyCell>
                    <TableBodyCell>{email.body.slice(0, 20) + '...'}</TableBodyCell>
                    <TableBodyCell>{email.type}</TableBodyCell>
                    <TableBodyCell>
                        <Button color="primary" size="sm" outline on:click={editEmail(email.id)}>Edit</Button>
                    </TableBodyCell>
                </TableBodyRow>
            {/each}
        </TableBody>
    </Table>
</div>

<Modal title="Edit Email Type" bind:open={emailModal} autoclose>
    <div class="mb-6">
        <FloatingLabelInput id="floating_standard" name="floating_standard" type="text" style="outlined" bind:value={email.subject}>
            Subject
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <Label>Content</Label>
        <Textarea bind:value={email.body} rows={8} />
    </div>

    <Button class="w-full" on:click={saveEmail}>Save</Button>
</Modal>