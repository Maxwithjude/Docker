import { createRouter, createWebHashHistory } from "vue-router";
import StorageView from "../views/StorageView.vue";
import AlarmView from "../views/AlarmView.vue";
import FeedView from "../views/FeedView.vue";

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: "/",
      redirect: "/storage",
    },
    {
      path: "/storage",
      name: "storage",
      component: StorageView,
    },
    {
      path: "/alarm",
      name: "alarm",
      component: AlarmView,
    },
    {
      path: "/feed",
      name: "feed",
      component: FeedView,
    },
  ],
});

export default router;
