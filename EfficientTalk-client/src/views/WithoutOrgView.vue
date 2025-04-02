<template>
  <div class="without-org-view-container">
    <EmptyContainer :description="'您还没有加入组织噢，快去加入组织解锁此功能吧'">
      <template #extra>
        <div style="display: flex;align-items: center;gap: 50px">
          <a-button type="primary"
                    @click="handleOrgCreatorDialogOpen('CREATE')"
          >
            创建组织
          </a-button>
          <a-button type="primary"
                    @click="handleOrgCreatorDialogOpen('JOIN')"
          >
            加入组织
          </a-button>
        </div>
      </template>
    </EmptyContainer>
  </div>

  <!--创建/加入组织对话框-->
  <OrgCreatorDialog ref="orgCreatorDialog"/>
</template>

<script setup>
    import EmptyContainer from "../components/empty-container/EmptyContainer.vue";
    import OrgCreatorDialog from "../components/dialog/module-social/org-creator/OrgCreatorDialog.vue";
    import { ref } from "vue";

    const orgCreatorDialog = ref();
    const handleOrgCreatorDialogOpen = (mode) => {
        orgCreatorDialog.value.dialogOpen(mode);
    };
</script>

<style scoped
       lang="scss"
>
  @use "/src/assets/style/global-variable.scss";

  .without-org-view-container {
    display: flex;
    width: 100%;
    height: 100%;
    border-top: global-variable.$border-line-width solid global-variable.$border-line-color;
    background-color: global-variable.$background-color;
  }
</style>