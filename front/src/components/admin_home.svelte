<script>
    import {DarkMode, Toggle, Textarea, Label, FloatingLabelInput, Modal, Card, TabItem, Tabs, Table, TableBody, TableBodyCell, TableBodyRow, TableHead, TableHeadCell, Checkbox, TableSearch, Button} from 'flowbite-svelte';
    import {onMount} from "svelte";
    import Clients from "./admin/clients.svelte";
    import Emails from './admin/emails.svelte';
    import Trainingtypes from './admin/trainingtypes.svelte';
    import Gyms from './admin/gyms.svelte';
    import Managers from './admin/managers.svelte';
    import Notifications from './admin/notifications.svelte';

    let fetchClients, fetchEmails, fetchTrainingTypes, fetchGyms, fetchManagers, fetchNotifications;

    onMount(async () => {
        fetchClients();
    });
</script>
<DarkMode />
<Card class="ml-auto mr-auto bg-gray-50" size="xl" padding="none">
    <Tabs style="underline">
        <TabItem open title="Clients" on:click={fetchClients}>
            <Clients bind:fetchData={fetchClients} />
        </TabItem>
        <TabItem title="Emails" on:click={fetchEmails}>
            <Emails bind:fetchData={fetchEmails} />
        </TabItem>
        <TabItem title="Training Types" on:click={fetchTrainingTypes}>
            <Trainingtypes bind:fetchData={fetchTrainingTypes} />
        </TabItem>
        <TabItem title="Gyms" on:click={fetchGyms}>
            <Gyms bind:fetchData={fetchGyms} />
        </TabItem>
        <TabItem title="Managers" on:click={fetchManagers}>
            <Managers bind:fetchData={fetchManagers} />
        </TabItem>
        <TabItem title="Notifications" on:click={fetchNotifications}>
            <Notifications bind:fetchData={fetchNotifications} />
        </TabItem>
    </Tabs>
</Card>
