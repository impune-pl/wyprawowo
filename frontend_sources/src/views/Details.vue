<template>
    <b-container>
        <b-overlay v-if="this.loadingHike" :show="true" no-wrap>
        </b-overlay>
        <b-card v-else>
            <b-card-title>Hike length: {{hike.lengthInKm}}</b-card-title>
            <b-card-sub-title>Started on: {{hike.startDate}}</b-card-sub-title>
            <b-carousel
                id="carousel-1"
                v-model="slide"
                :interval="4000"
                controls
                indicators
                img-width="1024"
                img-height="480"
                style="text-shadow: 1px 1px 2px #333;"
                @sliding-start="onSlideStart"
                @sliding-end="onSlideEnd"
            >
              <b-carousel-slide v-for="image in this.hike.images" v-bind:key="image.id" v-bind:img-src="'/api/image/'+image.id"></b-carousel-slide>
            </b-carousel>
            <b-button v-b-toggle.collapse-1 variant="link"><h4>Video</h4></b-button>
            <b-collapse id="collapse-1" class="mt-2">
              <video  width="480" autoplay muted controls>
                <source v-bind:src="'/api/video/'+this.hike.video.id" type="video/mp4">
              </video>
            </b-collapse>
            <b-card-body>
                <b-card-text>
                    <p>
                      From: {{hike.startCoordinates}}
                    </p>
                    <p>
                      To: {{hike.endCoordinates}}
                    </p>
                </b-card-text>
            </b-card-body>
        </b-card>
    </b-container>
</template>

<script>
    export default {
        name: "Details",
        data()
        {
            return{ hike: null, id: null, isLoggedIn: false, isAdmin: false, loadingHike:true,slide: 0, sliding:null};
        },
        beforeMount()
        {
            this.loadHike();
            let user = JSON.parse(localStorage.getItem('hike-site.user'));
            if(user) {
              this.isLoggedIn = true;
            }
            else
            {
                this.isLoggedIn=false;
            }
        },
        methods : {
            loadHike()
            {
                this.id = this.$route.params.id;
                window.axios.get('/api/hike/'+this.id).then(res => {
                    this.hike = res.data;
                    this.loadingHike=false;
                }).catch(()=>console.log("error loading hike data"));
            },
          onSlideStart(slide) {
            this.slide=slide;
            this.sliding = true
          },
          onSlideEnd(slide) {
            this.slide=slide;
            this.sliding = false
          }
        }
    }
</script>

<style scoped>
    .text-danger-on-hover:hover
    {
        color:#e3342f;
    }
    .text-danger-on-hover
    {
        color:#6c757d;
    }
</style>
