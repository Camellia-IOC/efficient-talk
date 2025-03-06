<template>
  <div class="file-preview-view-container">
    <!--<vue-office-pdf style="width: 100%;height: 100vh;overflow-y: auto"-->
    <!--                src="http://47.122.40.126:8089/two_pigs_story/resource/%E5%9B%BD%E5%AE%B6%E7%94%B5%E7%BD%91%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E4%BC%81%E4%B8%9A%E6%96%87%E5%8C%96%E3%80%81%E7%94%B5%E5%8A%9B%E4%B8%8E%E8%83%BD%E6%BA%90%E6%88%98%E7%95%A5%E9%A2%98%E5%BA%93-2025%E7%89%88.pdf"-->
    <!--                @rendered="renderHandler"-->
    <!--                @error="errorHandler"-->
    <!--&gt;</vue-office-pdf>-->
    <!--<vue-office-excel style="width: 100%;height: 100vh;overflow-y: auto"-->
    <!--                  src="http://47.122.40.126:8089/two_pigs_story/resource/%E8%BD%AF1-%E5%90%8D%E5%8D%95.xlsx"-->
    <!--                  @rendered="renderHandler"-->
    <!--                  @error="errorHandler"-->
    <!--&gt;</vue-office-excel>-->
    <!--<vue-office-docx style="width: 100%;height: 100vh;overflow-y: auto"-->
    <!--                 src="http://47.122.40.126:8089/two_pigs_story/resource/%E8%BD%AF1-%E8%8A%B1%E5%90%8D%E5%86%8C.docx"-->
    <!--                 @rendered="renderHandler"-->
    <!--                 @error="errorHandler"-->
    <!--&gt;</vue-office-docx>-->
    <vue-office-pptx style="width: 100%;height: 100vh;overflow-y: auto"
                     src="http://47.122.40.126:8089/two_pigs_story/resource/%E7%AC%AC%E5%85%AB%E7%BB%84a%E5%86%B2%E5%88%BA%E7%AD%94%E8%BE%A9.pptx"
                     @rendered="renderHandler"
                     @error="errorHandler"
    ></vue-office-pptx>
  </div>
</template>

<script setup>
    import VueOfficePdf from "@vue-office/pdf";
    import VueOfficeDocx from "@vue-office/docx";
    import VueOfficeExcel from "@vue-office/excel";
    import VueOfficePptx from "@vue-office/pptx";
    import "@vue-office/docx/lib/index.css";
    import "@vue-office/excel/lib/index.css";

    const renderHandler = () => {
        console.log("rendered");
    };

    const errorHandler = () => {
        console.log("error");
    };
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .file-preview-view-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: global-variable.$background-color;
  }
</style>