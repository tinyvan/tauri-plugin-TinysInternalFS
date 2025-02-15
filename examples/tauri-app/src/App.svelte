<script lang="ts">
  import {
    readFileImmediately,
    writeFileImmediately,
    // readFileAll,
    // writeFile,
    // openFile,
    // closeFile,
  } from "tauri-plugin-tinys-internal-fs-api";
  import { TFile } from "tauri-plugin-tinys-internal-fs-api";
  let response = "";
  let file:TFile|null = null;
  function updateResponse(returnValue) {
    response +=
      `[${new Date().toLocaleTimeString()}] ` +
      (typeof returnValue === "string"
        ? returnValue
        : JSON.stringify(returnValue)) +
      "<br>";
  }

  function testopenRead() {
    file=new TFile("test2.txt","r");
    file.readAll().then((data) => {
      console.log("read:",data) 
    })
    file.close();
  }
  function testopenWrite() {
    file=new TFile("test2.txt","w");
    file.write("AAA world").then((data) => {
      console.log("write:",data)
    }).catch((e)=>{
      console.log("write error:",e)
    })
    file.close();
  }
</script>

<main class="container">

  <div>
    <button on:click={testopenRead}>read</button>
    <div>{@html response}</div>
  </div>
  <div>
    <button on:click={testopenWrite}>write</button>
  </div>
</main>

<style>

</style>
