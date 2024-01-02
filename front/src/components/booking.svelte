<script>
    import FullCalendar, { Draggable } from 'svelte-fullcalendar';
	import daygridPlugin from '@fullcalendar/daygrid';
	import timegridPlugin from '@fullcalendar/timegrid';
	import interactionPlugin from '@fullcalendar/interaction';
    import { FloatingLabelInput, Modal, Button, Select, Label, Alert, Toast } from 'flowbite-svelte';
    import { CloseCircleSolid } from 'flowbite-svelte-icons';
    import axios from 'axios';
    import {get} from 'svelte/store';
    import {user} from '../lib/user.js';
    import { onMount } from 'svelte';

    const today = new Date();
    const firstDayOfWeek = new Date(today.setDate(today.getDate() - today.getDay() + 1)); // Set to the start of the current week (Monday)
    const lastDayOfWeek = new Date(firstDayOfWeek);
    lastDayOfWeek.setDate(lastDayOfWeek.getDate() + 6)

    let trainingTypes = [];
    let gyms = [];
    let sessions = [];
    let errorText = "";
    let gymSelectionDisabled = false;
    let errorMsg = "";
    let errorTimeout = null;

    const setErrorMessage = (message) => {
        errorMsg = message;
        clearTimeout(errorTimeout);
        errorTimeout = setTimeout(() => {
            errorMsg = "";
        }, 5000);
    }


    let addModal = false;
    let cancelModal = false;
    let bookModal = false;
    let managerCancelModal = false;
    let session = {
        "dayOfWeek": 0,
        "endHour": 0,
        "endMinute": 0,
        "gymId": 0,
        "maxParticipants": 0,
        "startHour": 0,
        "startMinute": 0,
        "trainingTypeId": 0
    }

    let selectedGym = {
        "id": 0,
        "name": "",
        "description": "",
        "managerId": 0,
        "numberOfTrainers": 0,
        "trainingTypes": []
    }

    function book() {
        let maxParticipants = 1;
        for (let i = 0; i < trainingTypes.length; i++) {
            if (trainingTypes[i].id == session.trainingTypeId) {
                if (trainingTypes[i].group) {
                    maxParticipants = 12;
                }
            }
        }
        let data = {
            "dayOfWeek": session.dayOfWeek,
            "endHour": session.endHour,
            "endMinute": session.endMinute,
            "gymId": selectedGym.id,
            "maxParticipants": maxParticipants,
            "startHour": session.startHour,
            "startMinute": session.startMinute,
            "trainingTypeId": session.trainingTypeId
        }

        axios.post('http://localhost:8084/trainings/training/session', data, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            bookModal = false;
            fetchSessionForGym();
        }).catch((error) => {
            setErrorMessage(error.response.data.message);
        });
    }

    function fetchTrainingTypes() {

        axios.get('http://localhost:8084/trainings/training/types', {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            trainingTypes = response.data;
        }).catch((error) => {
            setErrorMessage(error.response.data.message);
        });
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

    async function fetchGyms() {
        axios.get('http://localhost:8084/trainings/gym', {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then(async (response) => {
            gyms = response.data;
            if (selectedGym.id == 0) {
                selectedGym = gyms[0];
            }
            console.log('gyms', $user);
            if ($user.role == 'ROLE_MANAGER') {
                gymSelectionDisabled = true;
                let manager = await getManager();
                console.log('manager', manager);
                for (let i = 0; i < gyms.length; i++) {
                    if (gyms[i].managerId == manager.gymId) {
                        selectedGym = gyms[i];
                    }
                }
            }
            fetchSessionForGym();
        }).catch((error) => {
            setErrorMessage(error.response.data.message);
        });
    }

    function fetchSessionForGym() {
        for (let i = 0; i < gyms.length; i++) {
            if (gyms[i].id == selectedGym.id) {
                selectedGym = gyms[i];
            }
        }
        console.log(selectedGym);
        axios.get('http://localhost:8084/trainings/training/session/gym/' + selectedGym.id, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            sessions = response.data;
            updateEvents();
        }).catch((error) => {
            setErrorMessage(error.response.data.message);
        });
    }

    function addBook() {
        axios.post('http://localhost:8084/trainings/training/session/book', {
            clientId: get(user).id,
            trainingSessionId: session.id
        }, {headers: {Authorization: 'Bearer ' + get(user).token}}).then((response) => {
            console.log(response);
            addModal = false;
            fetchSessionForGym();
        }).catch((error) => {
            errorText = error.response.data.message;
        });
    }

    function cancel() {
        axios.post('http://localhost:8084/trainings/training/session/cancel',{
            clientId: get(user).id,
            trainingSessionId: session.id
        }, {headers: {Authorization: 'Bearer ' + get(user).token}}).then((response) => {
            console.log(response);
            cancelModal = false;
            fetchSessionForGym();
        }).catch((error) => {
            setErrorMessage(error.response.data.message);
        });
    }

    function updateEvents() {
        let events = [];
        // dayofweek +1
        for (let i = 0; i < sessions.length; i++) {
            let session = sessions[i];
            let start = new Date();
            start.setDate(start.getDate() + (session.dayOfWeek - start.getDay() + 7) % 7);
            start.setHours(session.startHour);
            start.setMinutes(session.startMinute);
            let end = new Date();
            end.setDate(end.getDate() + (session.dayOfWeek - end.getDay() + 7) % 7);
            end.setHours(session.endHour);
            end.setMinutes(session.endMinute);
            // console.log(session);
            events.push({
                title: session.trainingType.name,
                startTime: addLeadingZero(session.startHour) + ":" + addLeadingZero(session.startMinute) + ":00",
                endTime: addLeadingZero(session.endHour) + ":" + addLeadingZero(session.endMinute) + ":00",
                // additional
                extendedProps: {
                    session: session
                },
                daysOfWeek: [session.dayOfWeek],
                // from 2023
                startRecur: new Date(2023, 0, 1),
                // blue available, orange full, red cancelled
                color: session.canceled ? '#ff0000' : session.bookings.length >= session.maxParticipants ? '#ffa500' : '#3788d8',
            });

            console.log(session);

            
        }
        console.log('events', events);
        
        options = {
            ...options,
            events: events
        }
    
    }

	let options = {
		// dateClick: handleDateClick,
		droppable: true,
		events: [
			// initial event data
			{ title: 'New Event', start: new Date() },
		],
		initialView: 'timeGridWeek',
		plugins: [daygridPlugin, timegridPlugin, interactionPlugin],
		headerToolbar: {
			left: '',
			center: '',
			right: '',
		},
		height: '100%',
        allDaySlot: false,
        firstDay: 1,
        slotMinTime: "08:00:00",
        slotMaxTime: "20:00:00",
        slotDuration: "00:15:00",
        eventClick: function(arg) {
            if ($user.role == 'ROLE_MANAGER') {
                session = arg.event.extendedProps.session;
                managerCancelModal = true;
                return;
            }
            console.log(arg.event);
            session = arg.event.extendedProps.session;
            let found = false;
            session.bookings.forEach(book => {
                if (book.clientId == get(user).id) {
                    found = true;
                }
            });
            if (found) {
                cancelModal = true;
            } else {
                addModal = true;
            }
        },
        eventContent: function(arg) {
            if (!arg.event.extendedProps.session?.canceled) {
                return { html: `<div>${arg.event.title}</div><div class="hr-line-solid-no-margin"></div><span style="font-size: 10px">Participants ${arg.event.extendedProps.session?.bookings?.length || '0'} / ${arg.event.extendedProps.session?.maxParticipants || 0}</span></div>` }
            } else {
                return { html: `<div><del>${arg.event.title}</del></div><div class="hr-line-solid-no-margin"></div><span style="font-size: 10px">Participants ${arg.event.extendedProps.session?.bookings?.length || '0'} / ${arg.event.extendedProps.session?.maxParticipants || 0}</span></div>` }
            }
        },
        selectable: true,
        select: function(info) {
            console.log(info);
            if ($user.role == 'ROLE_MANAGER') {
                return;
            }
            var start = info.start;
            var end = info.end;

            var startData = {
                weekDay: start.getDay(),
                hour: start.getHours(),
                minute: start.getMinutes()
            }

            var endData = {
                weekDay: end.getDay(),
                hour: end.getHours(),
                minute: end.getMinutes()
            }

            session.dayOfWeek = startData.weekDay;
            session.startHour = startData.hour;
            session.startMinute = startData.minute;
            session.endHour = endData.hour;
            session.endMinute = endData.minute;

            bookModal = true;
        }
	};
	let calendarComponentRef;

    onMount(async () => {
        fetchTrainingTypes();
        fetchGyms();
    });

    let addLeadingZero = (number) => {
        if (number < 10) {
            return "0" + number;
        }
        return number;
    }

    let getDayOfWeek = (number) => {
        switch (number) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday"
            case 3:
                return "Wednesday"
            case 4:
                return "Thursday"
            case 5:
                return "Friday"
            case 6:
                return "Saturday"
        }
    }

    function cancelManager() {
        axios.post('http://localhost:8084/trainings/training/session/cancel/' + session.id, {}, {
            headers: {
                Authorization: 'Bearer ' + get(user).token,
            }
        }).then((response) => {
            console.log(response);
            managerCancelModal = false;
            fetchSessionForGym();
        }).catch((error) => {
            setErrorMessage(error.response.data.message);
        });
    }

</script>
<div class="w-1/4 text-left">
    <Label class="ml-auto mr-auto mt-4 mb-4">Select gym</Label>
    <Select bind:disabled={gymSelectionDisabled} on:change={fetchSessionForGym} bind:value={selectedGym.id} items={gyms.map((val) => {
        return { value: val.id, name: val.name };
    })}></Select>
</div>
<div class="demo-app">
	<div class="demo-app-calendar">
		<FullCalendar bind:this={calendarComponentRef} {options} />
	</div>
</div>

<Modal title="Book" bind:open={bookModal}>
    <div class="mb-6 text-left">
        <Label class="mb-2">Training Type</Label>
        <Select bind:value={session.trainingTypeId} items={selectedGym.trainingTypes.map((val) => {
            return { value: val.id, name: val.name };
        })}></Select>
    </div>
    <div class="flex">
        <div class="mb-6 w-1/2 mr-4">
            <FloatingLabelInput bind:value={selectedGym.name} id="floating_standard" name="floating_standard" type="text" style="outlined" disabled>
                Gym
            </FloatingLabelInput>
        </div>
        <div class="mb-6 w-1/2">
            <FloatingLabelInput bind:value={session.dayOfWeek} id="floating_standard" name="floating_standard" type="text" style="outlined" >
                Day of week
            </FloatingLabelInput>
        </div>
    </div>
    <div class="flex">
        <div class="mb-6 w-1/2 mr-4">
            <FloatingLabelInput bind:value={session.startHour} id="floating_standard" name="floating_standard" type="text" style="outlined" >
                Start hour
            </FloatingLabelInput>
        </div>
        <div class="mb-6 w-1/2">
            <FloatingLabelInput bind:value={session.startMinute} id="floating_standard" name="floating_standard" type="text" style="outlined" >
                Start minute
            </FloatingLabelInput>
        </div>
    </div>

    <div class="flex">
        <div class="mb-6 w-1/2 mr-4">
            <FloatingLabelInput bind:value={session.endHour} id="floating_standard" name="floating_standard" type="text" style="outlined" >
                End hour
            </FloatingLabelInput>
        </div>
        <div class="mb-6 w-1/2">
            <FloatingLabelInput bind:value={session.endMinute} id="floating_standard" name="floating_standard" type="text" style="outlined" >
                End minute
            </FloatingLabelInput>
        </div>
    </div>
    <div class="">
        <Button color="primary" class="ml-auto mr-auto w-full" on:click={book}>Book</Button>
    </div>
</Modal>

<Modal title="Book session" bind:open={addModal} on:close={errorText = ''}>
    {#if errorText != ""}
        <Alert color="red" class="text-left">
            {errorText}
        </Alert>
    {/if}
    <div>
        Book session for <b>{session.trainingType.name}</b> at {session.gym.name} on <b>{getDayOfWeek(session.dayOfWeek)}</b> from <b>{addLeadingZero(session.startHour)}:{addLeadingZero(session.startMinute)}</b> to <b>{addLeadingZero(session.endHour)}:{addLeadingZero(session.endMinute)}</b>?
    <div class="mt-8">
        <Button color="primary" class="ml-auto mr-auto w-full" on:click={addBook}>Book</Button>
    </div>
</Modal>
<Modal title="Cancel session" bind:open={cancelModal}>
    <div>
        Cancel session for <b>{session.trainingType.name}</b> at {session.gym.name} on <b>{getDayOfWeek(session.dayOfWeek)}</b> from <b>{addLeadingZero(session.startHour)}:{addLeadingZero(session.startMinute)}</b> to <b>{addLeadingZero(session.endHour)}:{addLeadingZero(session.endMinute)}</b>?
    <div class="mt-8">
        <Button color="primary" class="ml-auto mr-auto w-full" on:click={cancel}>Cancel</Button>
    </div>
</Modal>
<Modal title="Cancel session" bind:open={managerCancelModal}>
    <div>
        Cancel session for <b>{session.trainingType.name}</b> at {session.gym.name} on <b>{getDayOfWeek(session.dayOfWeek)}</b> from <b>{addLeadingZero(session.startHour)}:{addLeadingZero(session.startMinute)}</b> to <b>{addLeadingZero(session.endHour)}:{addLeadingZero(session.endMinute)}</b>?
    <div class="mt-8">
        <Button color="primary" class="ml-auto mr-auto w-full" on:click={cancelManager}>Cancel</Button>
    </div>
</Modal>

{#if errorMsg != ""}
<Toast color="red" contentClass="font-semibold text-sm" class="fixed opacity-100 bg-opacity-100 z-20 text-red-900 font-bold" dismissable={false} position="bottom-right">
    <svelte:fragment slot="icon">
    <CloseCircleSolid class="w-5 h-5" />
    <span class="sr-only">Error icon</span>
  </svelte:fragment>
  <span class="font-md font-semibold">{errorMsg}</span>
    
</Toast>
{/if}

<style>
	:global(* > *) {
		padding: 0;
		margin: 0;
		box-sizing: border-box;
	}

	.demo-app {
		height: 100vh;
		padding: 0.5rem;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
		font-size: 14px;
	}

	.demo-app-calendar {
		width: 100%;
		flex-grow: 1;
	}
	:global(.draggable) {
		color: white;
		background: #3788d8;
		width: fit-content;
		padding: 1rem;
		margin: 1rem;
		cursor: pointer;
	}
</style>