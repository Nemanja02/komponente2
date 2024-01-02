<script>
    import {Table, TableHead, TableHeadCell, TableBody, TableBodyRow, TableBodyCell, Button, Checkbox, Modal, Label, FloatingLabelInput, Select} from "flowbite-svelte";
    import axios from "axios";
    import {get} from "svelte/store";
    import {user} from "../../lib/user.js";

    let trainingTypes = []
    let editModal = false;
    let addModal = false;
    let priceModal = false;

    let pricing = {
        "price": 0,
        "trainingTypeId": 0,
        id: 0,
    }

    let trainingType = {
        "id": '',
        "name": "",
        "group": false,
    }

    export const fetchData = () => {
        axios.get("http://localhost:8084/trainings/training/types", {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            trainingTypes = response.data;
            console.log(trainingTypes);
        }).catch((error) => {
            console.log(error);
        });
    }

    function edit(id) {
        axios.get("http://localhost:8084/trainings/training/types/" + id, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            trainingType = response.data;
            editModal = true;
        }).catch((error) => {
            console.log(error);
        });
    }

    function save() {
        axios.put("http://localhost:8084/trainings/training/types/" + trainingType.id, {
            "name": trainingType.name,
            "group": trainingType.group,
        }, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            editModal = false;
            fetchData();
        }).catch((error) => {
            console.log(error);
        });
    }


    function add() {
        axios.post("http://localhost:8084/trainings/training/types", {
            "name": trainingType.name,
            "group": trainingType.group,
        }, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            addModal = false;
            fetchData();
        }).catch((error) => {
            console.log(error);
        });
    }

    function addPrice() {
        if (pricing.id == 0) {
            axios.post("http://localhost:8084/trainings/training/pricings", {
                "price": pricing.price,
                "trainingTypeId": pricing.trainingTypeId,
            }, {
                headers: {
                    Authorization: "Bearer " + get(user).token,
                }
            }).then((response) => {
                console.log(response);
                priceModal = false;
                fetchData();
            }).catch((error) => {
                console.log(error);
            });
            return;
        }
        axios.put("http://localhost:8084/trainings/training/pricings/" + pricing.id, {
            "price": pricing.price,
            "trainingTypeId": pricing.trainingTypeId,
        }, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            priceModal = false;
            fetchData();
        }).catch((error) => {
            console.log(error);
        });
    }

    function remove(id) {
        axios.delete("http://localhost:8084/trainings/training/types/" + id, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            fetchData();
        }).catch((error) => {
            console.log(error);
        });
    }
</script>

<div>
    <Button color="green" size="xs" class="mb-6" on:click={() => {
        trainingType = {
            "name": "",
            "group": false,
        }
        addModal = true;
    }}>Add</Button>
    <Table>
        <TableHead>
            <TableHeadCell>Id</TableHeadCell>
            <TableHeadCell>Name</TableHeadCell>
            <TableHeadCell>Price</TableHeadCell>
            <TableBodyCell>Group</TableBodyCell>
            <TableHeadCell>Actions</TableHeadCell>
        </TableHead>
        <TableBody>
            {#each trainingTypes as trainingType}
                <TableBodyRow>
                    <TableBodyCell>{trainingType.id}</TableBodyCell>
                    <TableBodyCell>{trainingType.name}</TableBodyCell>
                    <TableBodyCell>{trainingType.pricing?.price || 0}</TableBodyCell>
                    <TableBodyCell>
                        <Checkbox checked={trainingType.group} disabled />
                    </TableBodyCell>
                    <TableBodyCell>
                        <Button color="primary" size="xs" outline on:click={edit(trainingType.id)}>Edit</Button>
                        <Button color="blue" size="xs" outline on:click={() => {
                            pricing = {
                                "price": trainingType.pricing?.price || 0,
                                "trainingTypeId": trainingType.id,
                                id: trainingType.pricing?.id || 0,
                            }
                            priceModal = true;
                        }}>Change Price</Button>
                        <Button color="red" size="xs" on:click={remove(trainingType.id)}>Delete</Button>
                    
                    </TableBodyCell>
                </TableBodyRow>
            {/each}
        </TableBody>
    </Table>
</div>

<Modal title="Edit Training Type" bind:open={editModal}>
    <div class="mb-6">
        <FloatingLabelInput id="name" name="name" type="text" style="outlined" bind:value={trainingType.name}>
            Name
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <Label>Group</Label>
        <Checkbox bind:checked={trainingType.group} />
    </div>
    <div class="mb-6">
        <Button color="green" size="sm" on:click={save}>Save</Button>
    </div>
</Modal>

<Modal title="Add Training Type" bind:open={addModal}>
    <div class="mb-6">
        <FloatingLabelInput id="name" name="name" type="text" style="outlined" bind:value={trainingType.name}>
            Name
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <Label>Group</Label>
        <Checkbox bind:checked={trainingType.group} />
    </div>
    <div class="mb-6">
        <Button color="green" size="sm" on:click={add}>Add</Button>
    </div>
</Modal>

<Modal title="Change Price" bind:open={priceModal}>
    <div class="mb-6">
        <FloatingLabelInput id="name" name="name" type="number" style="outlined" bind:value={pricing.price}>
            Price
        </FloatingLabelInput>
    </div>
    <Select bind:value={pricing.trainingTypeId} items={trainingTypes.map((val) => {
        return { value: val.id, name: val.name };
    })} >
    </Select>
    <div class="mb-6">
        <Button color="green" size="sm" on:click={addPrice}>Save</Button>
    </div>
</Modal>