import { createRouter, createWebHistory } from 'vue-router'
import IntroView from '@/views/IntroView.vue'
import LogInView from '@/views/UserViews/LogInView.vue'
import RegisterView from '@/views/UserViews/RegisterView.vue'
import CollectionSelectView from '@/views/UserViews/CollectionSelectView.vue'
import TagSelectView from '@/views/UserViews/TagSelectView.vue'
import MainView from '@/views/MainView.vue'
import ImportantView from '@/views/ImportantView.vue'
import RecommendedView from '@/views/RecommendedView.vue'
import PersonalView from '@/views/PersonalView.vue'
import SharedView from '@/views/SharedView.vue'
import SearchView from '@/views/SearchView.vue'
import FeedView from '@/views/FeedView.vue'
import OldView from '@/views/OldView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 소개페이지 : 첫 화면
    {
      path: '/',
      name: 'intro',
      component: IntroView,
    },
    // 로그인 페이지 
    {
      path: '/login',
      name: 'login',
      component: LogInView,
    },
    // 회원가입 페이지 
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    // 컬렉션 선택 페이지 : 최초 로그인일 때만
    {
      // 추후 유저 로그인 개발하면 /user/:id 추가하기
      path: '/collectionSelect',
      name: 'collectionSelect',
      component: CollectionSelectView,
    },
    // 태그 선택 페이지 : 최초 로그인일 때만
    {
      // 추후 유저 로그인 개발하면 /user/:id 추가
      path: '/tagSelect',
      name: 'tagSelect',
      component: TagSelectView,
    },
    // 메인 페이지 : 로그인 후 개인의 메인 페이지
    {
      path: '/user/:id/main',
      name: 'main',
      component: MainView,
    },
    // 중요페이지
    {
      path: '/user/:id/important',
      name: 'important',
      component: ImportantView,
    },
    // 추천페이지
    {
      path: '/user/:id/recommended',
      name: 'recommended',
      component: RecommendedView,
    },
    // 개인 컬렉션
    {
      path: '/user/:id/personal',
      name: 'personal',
      component: PersonalView,
    },
    // 공유 컬렉션
    {
      path: '/user/:id/shared',
      name: 'shared',
      component: SharedView,
    },
    // 검색 페이지
    {
      path: '/user/:id/search',
      name: 'search',
      component: SearchView,
    },
    // 피드 페이지
    {
      path: '/user/:id/feed',
      name: 'feed',
      component: FeedView,
    },  
    // 오래된 북마크 페이지
    {
      path: '/user/:id/old',
      name: 'old',
      component: OldView,
    },
  
  ],
})

export default router
