<script>
    import {Table, TableHead, TableHeadCell, TableBody, TableBodyRow, TableBodyCell, Button, Checkbox, Modal, FloatingLabelInput, Toggle, Label, Select} from "flowbite-svelte";
    import axios from "axios";
    import {get} from "svelte/store";
    import {user} from "../../lib/user.js";
    let notifications = [];
    let shown = [];
    let openRow
    let details


    const toggleRow = (i) => {
        openRow = openRow === i ? null : i
    }

    export const fetchData = () => {
        axios.post("http://localhost:8084/notifications/user/" + $user.id, {
            role: $user.role
        }, {
            headers: {
                Authorization: "Bearer " + get(user).token,
            }
        }).then((response) => {
            notifications = response.data;
            updateFilters();
        }).catch((error) => {
            console.log(error);
        });
    }

    let notifTypes = [
        'ALL',
        'ACTIVATION',
        'PASSWORD_CHANGED',
        'TRAINING_SESSION_CANCELLED',
        'TRAINING_SESSION_BOOKED',
        'TRAINING_SESSION_CANCELLED_BY_MANAGER',
        'TRAINING_SESSION_CANCELLED_BY_CLIENT',
        'TRAINING_SESSION_BOOKED_BY_CLIENT',
        'TRAINING_SESSION_REMINDER',
    ]

    let orders = [
        'Time ascending',
        'Time descending',
    ];
    let order = orders[0];

    let filters = {
        type: 'ALL',
        role: 'ALL',
        timeFrom: '',
        timeTo: '',
    }

    function updateFilters() {
        shown = notifications.filter((notif) => {
            let show = true;
            if (filters.type !== 'ALL') {
                console.log(notif.type, filters.type);
                show = show && notif.type == filters.type;
            }
            if (filters.role !== 'ALL') {
                show = show && notif.role == filters.role;
            }
            if (filters.timeFrom !== '') {
                show = show && notif.bookedAt >= filters.timeFrom;
            }
            if (filters.timeTo !== '') {
                show = show && notif.bookedAt <= filters.timeTo;
            }
            return show;
        });
        if (order === orders[0]) {
            shown.sort((a, b) => {
                return new Date(a.bookedAt) - new Date(b.bookedAt);
            });
        } else {
            shown.sort((a, b) => {
                return new Date(b.bookedAt) - new Date(a.bookedAt);
            });
        }
    }

    function dateAgo(date) {
        let now = new Date();
        let then = new Date(date);
        let diff = now - then;
        let seconds = Math.floor(diff / 1000);
        let minutes = Math.floor(seconds / 60);
        let hours = Math.floor(minutes / 60);
        let days = Math.floor(hours / 24);
        if (days > 0) {
            return days + ' days ago';
        }
        if (hours > 0) {
            return hours + ' hours ago';
        }
        if (minutes > 0) {
            return minutes + ' minutes ago';
        }
        if (seconds > 0) {
            return seconds + ' seconds ago';
        }
        return 'just now';
    
    }
</script>


<div>
    <div class="flex text-left">
        <div class="w-1/5 mr-4">
            <Label for="type">Order</Label>
            <Select bind:value={order} id="type" on:change={updateFilters}>
                {#each orders as o}
                    <option value={o}>{o}</option>
                {/each}
            </Select>
        </div>
        <div class="w-1/5 mr-4">
            <Label for="type">Type</Label>
            <Select bind:value={filters.type} id="type" on:change={updateFilters}>
                {#each notifTypes as t}
                    <option value={t}>{t}</option>
                {/each}
            </Select>
        </div>
        <div class="w-1/5 mr-4">
            <Label for="role">Role</Label>
            <Select bind:value={filters.role} id="role" on:change={updateFilters}>
                <option value="ALL">ALL</option>
                <option value="ROLE_USER">ROLE_USER</option>
                <option value="ROLE_MANAGER">ROLE_MANAGER</option>
                <option value="ROLE_ADMIN">ROLE_ADMIN</option>
            </Select>
        </div>

        <div class="w-1/5 mr-4">
            <Label for="timeFrom">Time from</Label>
            <FloatingLabelInput bind:value={filters.timeFrom} id="timeFrom" name="timeFrom" type="date" style="outlined" on:change={updateFilters} />
        </div>  

        <div class="w-1/5 mr-4">
            <Label for="timeTo">Time to</Label>
            <FloatingLabelInput bind:value={filters.timeTo} id="timeTo" name="timeTo" type="date" style="outlined" on:change={updateFilters} />
        </div>
    </div>
    <Table>
        <TableHead>
            <TableHeadCell>TYPE</TableHeadCell>
            <TableHeadCell>Subject</TableHeadCell>
            <TableHeadCell>UserID</TableHeadCell>
            <TableHeadCell>Role</TableHeadCell>
            <TableHeadCell>Time</TableHeadCell>
        </TableHead>
        <TableBody>
            {#each shown as notif, i}
                <TableBodyRow on:click={() => toggleRow(i)}>
                    <TableBodyCell>{notif.type}</TableBodyCell>
                    <TableBodyCell>{notif.subject}</TableBodyCell>
                    <TableBodyCell>{notif.userId}</TableBodyCell>
                    <TableBodyCell>{notif.role}</TableBodyCell>
                    <TableBodyCell>{dateAgo(notif.bookedAt)}</TableBodyCell>
                </TableBodyRow>
                {#if openRow === i}
                    <TableBodyRow>
                      <TableBodyCell colspan="4" class="p-0">
                        <div class="p-4">
                          <p>{notif.body}</p>
                        </div>
                        </TableBodyCell>
                    </TableBodyRow>
                {/if}
            {/each}
        </TableBody>
    </Table>
</div>