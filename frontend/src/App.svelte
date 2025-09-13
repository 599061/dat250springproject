
<script>
    let counter = 0;

    function increment() {
        counter = counter + 1;
    }
</script>

<h1>Well, Hello there.</h1>
<p>Counter is {counter}</p>
<button on:click={increment}>Click me</button>
<style>
    * {
       margin: 0;
        font-family: "Fira Sans", sans-serif;
    }
</style>