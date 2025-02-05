<template>
<div class="feed-tabs">
    <div 
    v-for="feed in feeds" 
    :key="feed.rss_id"
    :class="['tab', { active: selectedFeed === feed.rss_id }]"
    @click="$emit('select-feed', feed.rss_id)"
    >
    {{ feed.name }}
    <span v-if="!feed.is_read" class="unread-badge"></span>
    </div>
</div>
</template>

<script setup>
defineProps({
feeds: {
    type: Array,
    required: true
},
selectedFeed: {
    type: Number,
    required: true
}
})

defineEmits(['select-feed'])
</script>

<style scoped>
.feed-tabs {
display: flex;
gap: 1rem;
padding: 1rem;
background-color: #f5f5f5;
border-bottom: 1px solid #ddd;
}

.tab {
padding: 0.5rem 1rem;
cursor: pointer;
border-radius: 4px;
position: relative;
}

.tab.active {
background-color: #007bff;
color: white;
}

.unread-badge {
position: absolute;
top: -5px;
right: -5px;
width: 8px;
height: 8px;
background-color: #ff4444;
border-radius: 50%;
}
</style>