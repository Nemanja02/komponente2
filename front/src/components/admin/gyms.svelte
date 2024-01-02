<script>
    import {Toggle, Textarea, Label, FloatingLabelInput, Modal, Card, TabItem, Tabs, Table, TableBody, TableBodyCell, TableBodyRow, TableHead, TableHeadCell, Checkbox, TableSearch, Button, Badge, Select, MultiSelect} from 'flowbite-svelte';
    import axios from 'axios';

    import {get} from 'svelte/store';
    import {user} from '../../lib/user.js';

    let gymModal = false;
    let addGymModal = false;
    let gyms = [];
    let gym = {
        id: '',
        name: '',
        description: '',
        managerId: '',
        numberOfTrainers: '',
        trainingTypes: [],
    }
    let trainingTypes = [];
    let selectedTrainingTypes = [];

    export const fetchData = () => {
        axios.get('http://localhost:8084/trainings/gym', {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            gyms = response.data;
            console.log(gyms);
        }).catch((error) => {
            console.log(error);
        });
        fetchTrainingTypes();
    }

    function fetchTrainingTypes() {
        axios.get('http://localhost:8084/trainings/training/types', {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            trainingTypes = response.data;
        }).catch((error) => {
            console.log(error);
        });
    }

    function editGym(id) {
        axios.get('http://localhost:8084/trainings/gym/' + id, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            gym = response.data;
            selectedTrainingTypes = gym.trainingTypes.map((val) => val.id);
            gymModal = true;
        }).catch((error) => {
            console.log(error);
        });
    }

    function updateGym() {
        axios.put('http://localhost:8084/trainings/gym/' + gym.id, {
            name: gym.name,
            description: gym.description,
            managerId: gym.managerId,
            numberOfTrainers: gym.numberOfTrainers,
            trainingTypes: selectedTrainingTypes,
        }, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            gymModal = false;
            fetchData();
        }).catch((error) => {
            console.log(error);
        });
    }

    function deleteGym(id) {
        axios.delete('http://localhost:8084/trainings/gym/' + id, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
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
    <div class="mb-6">
        <Button color="green" size="xs" on:click={() => addGymModal = true}>Add Gym</Button>
    </div>
    <Table>
        <TableHead>
            <TableHeadCell>Id</TableHeadCell>
            <TableHeadCell>Name</TableHeadCell>
            <TableHeadCell>Description</TableHeadCell>
            <TableHeadCell>Manager Id</TableHeadCell>
            <TableHeadCell>Number of trainers</TableHeadCell>
            <TableHeadCell>Training types</TableHeadCell>
            <TableHeadCell>Actions</TableHeadCell>
        </TableHead>
        <TableBody>
            {#each gyms as gym}
                <TableBodyRow>
                    <TableBodyCell>{gym.id}</TableBodyCell>
                    <TableBodyCell>{gym.name}</TableBodyCell>
                    <TableBodyCell>{gym.description}</TableBodyCell>
                    <TableBodyCell>{gym.managerId}</TableBodyCell>
                    <TableBodyCell>{gym.numberOfTrainers}</TableBodyCell>
                    <TableBodyCell>
                        {#each gym.trainingTypes as trainingType}
                            <Badge class="mr-1">{trainingType.name}</Badge>
                        {/each}
                    </TableBodyCell>
                    <TableBodyCell>
                        <Button on:click={() => editGym(gym.id)}>Edit</Button>
                        <Button on:click={() => deleteGym(gym.id)}>Delete</Button>
                    </TableBodyCell>
                </TableBodyRow>
            {/each}
        </TableBody>
    </Table>
</div>

<Modal title="Edit Gym" bind:open={gymModal}>
    <div class="mb-6">
        <FloatingLabelInput bind:value={gym.name} id="floating_standard" name="floating_standard" type="text" style="outlined">
            Name
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={gym.description} id="floating_standard" name="floating_standard" type="text" style="outlined" >
            Description
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={gym.managerId} id="floating_standard" name="floating_standard" type="text" style="outlined" >
            Manager Id
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={gym.numberOfTrainers} id="floating_standard" name="floating_standard" type="text" style="outlined" >
            Number of trainers
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <Label>Training types</Label>
        <MultiSelect bind:value={selectedTrainingTypes} items={trainingTypes.map((val) => {
            return { value: val.id, name: val.name };
        })}></MultiSelect>
    </div>
    <div class="mb-6">
        <Button color="primary" on:click={updateGym}>Update</Button>
    </div>
</Modal>

<Modal title="Add Gym" bind:open={addGymModal}>
    <div class="mb-6">
        <FloatingLabelInput bind:value={gym.name} id="floating_standard" name="floating_standard" type="text" style="outlined">
            Name
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={gym.description} id="floating_standard" name="floating_standard" type="text" style="outlined" >
            Description
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={gym.managerId} id="floating_standard" name="floating_standard" type="text" style="outlined" >
            Manager Id
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <FloatingLabelInput bind:value={gym.numberOfTrainers} id="floating_standard" name="floating_standard" type="text" style="outlined" >
            Number of trainers
        </FloatingLabelInput>
    </div>
    <div class="mb-6">
        <Label>Training types</Label>
        <MultiSelect bind:value={selectedTrainingTypes} items={trainingTypes.map((val) => {
            return { value: val.id, name: val.name };
        })}></MultiSelect>
    </div>
    <div class="mb-6">
        <Button color="primary" on:click={() => {
            axios.post('http://localhost:8084/trainings/gym/add', {
                name: gym.name,
                description: gym.description,
                managerId: gym.managerId,
                numberOfTrainers: gym.numberOfTrainers,
                trainingTypes: selectedTrainingTypes,
            }, {
                headers: {
                    Authorization: 'Bearer ' + get(user).token,
                }
            }).then((response) => {
                console.log(response);
                addGymModal = false;
                fetchData();
            }).catch((error) => {
                console.log(error);
            });
        }}>Add</Button>
    </div>
</Modal>