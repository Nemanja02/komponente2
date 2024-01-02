<script>
    import { Table, TableBody, TableBodyCell, TableHead, TableHeadCell, TableBodyRow, Button,FloatingLabelInput,Modal,Textarea,Label } from "flowbite-svelte";
    import axios from "axios";
    import { get } from "svelte/store";
    import { user } from "../../lib/user.js";
    let rewards = []
    let rewardModal = false;
    let reward = {
        id: '1',
        gymId: '',
        threshold: '',
        discount: '',
        description: '',
    }

    async function getManager() {
        return new Promise((resolve) => {
            axios.get('http://localhost:8084/users/manager/' + get(user).id, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
            }).then((response) => {
                resolve(response.data);
            }).catch((error) => {
                setErrorMessage(error.response.data.message);
            });
        })
    }

    export const fetchData = async () => {
        let manager = await getManager();
        axios.get("http://localhost:8084/trainings/reward/" + manager.gymId, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            rewards = response.data;
        }).catch((error) => {
            console.log(error);
        });
    }

    function editReward(reward2) {
        reward = reward2;
        rewardModal = true;
        
        console.log(reward);
    }

    function saveReward(id) {
        axios.put('http://localhost:8084/trainings/reward/' + id,{
            "id": reward.id,
            "gymId": reward.gymId,
            "threshold": reward.threshold,
            "discount": reward.discount,
            "description": reward.description,
        }, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            reward = response.data;
            rewardModal = false;
            fetchData();
        }).catch((error) => {
            console.log(error);
        })
    }

    /*
    {
        "id": 1,
        "gymId": 1,
        "threshold": 3,
        "discount": 10,
        "description": "Popust od 10% na svaki treci trening"
    }
    */
</script>

<div>
    <Table>
        <TableHead>
            <TableHeadCell>Id</TableHeadCell>
            <TableHeadCell>Gym Id</TableHeadCell>
            <TableHeadCell>Threshold</TableHeadCell>
            <TableHeadCell>Discount %</TableHeadCell>
            <TableHeadCell>Description</TableHeadCell>
            <TableHeadCell>Edit</TableHeadCell>
        </TableHead>
        <TableBody>
            {#each rewards as reward2}
                <TableBodyRow>
                    <TableBodyCell>{reward2.id}</TableBodyCell>
                    <TableBodyCell>{reward2.gymId}</TableBodyCell>
                    <TableBodyCell>{reward2.threshold}</TableBodyCell>
                    <TableBodyCell>{reward2.discount}</TableBodyCell>
                    <TableBodyCell>{reward2.description}</TableBodyCell>
                    <TableBodyCell>
                        <Button color="green" size="xs" on:click={() => editReward(reward2)}>Edit</Button>
                    </TableBodyCell>
                </TableBodyRow>
            {/each}
        </TableBody>
    </Table>
</div>

<Modal title="Edit Loyalty Reward" bind:open={rewardModal} autoclose>
    <div class="mb-6">
        <FloatingLabelInput bind:value={reward.id} id="floating_standard" name="floating_standard" type="text" style="outlined" disabled>
            ID
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={reward.gymId} id="b" name="b" type="text" style="outlined" disabled>
            Gym Id
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={reward.threshold} id="c" name="c" type="text" style="outlined">
            Threshold
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={reward.discount} id="d" name="d" type="text" style="outlined">
            Discount
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={reward.description} id="e" name="e" type="text" style="outlined">
            Description
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <Button color="green" size="xs" on:click={() => saveReward(reward.id)}>Save</Button>
    </div>
</Modal>