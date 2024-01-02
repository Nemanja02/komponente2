<script>
    import { Card, TabItem, Tabs } from "flowbite-svelte";
    import Booking from "./booking.svelte";
    import UserNotifications from "./admin/user_notifications.svelte";
    import Rewards from "./admin/rewards.svelte";

    let fetchNotifications, fetchRewards;

</script>

<Card class=" mr-auto ml-auto mt-8 bg-gray-50 text-center w-4/5" size="xxl" padding="none">
    <Tabs style="underline">
        <TabItem open title="Booking">
            <Booking />
        </TabItem>
        <TabItem title="Notifications" on:click={fetchNotifications}>
            <UserNotifications bind:fetchData={fetchNotifications} />
        </TabItem>
        <TabItem title="Rewards" on:click={fetchRewards}>
            <Rewards bind:fetchData={fetchRewards} />
        </TabItem>
    </Tabs>
</Card>