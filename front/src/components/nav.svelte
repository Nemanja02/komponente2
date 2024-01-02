<script>
    import { user } from "../lib/user.js";
    import {
        Navbar,
        NavBrand,
        NavHamburger,
        NavUl,
        NavLi,
    } from "flowbite-svelte";
    let menu = [
        { name: "Home", href: "/" },
        { name: "Client register", href: "/register/client" },
        { name: "Manager register", href: "/register/manager" },
        { name: "Login", href: "/login" },
    ];

    if ($user.token) {
        menu = [
            { name: "Home", href: "/" },
            { name: "Logout", href: "/logout" },
        ];
    }
</script>

<Navbar let:hidden let:toggle>
    <NavBrand href="/">
        <span
            class="self-center whitespace-nowrap text-xl font-semibold dark:text-white"
            >Komponente</span
        >
    </NavBrand>
    <NavHamburger on:click={toggle} />
    <NavUl {hidden}>
        {#each menu as { name, href }}
        <NavLi href={href}>{name}</NavLi>
        {/each}
    </NavUl>
</Navbar>
