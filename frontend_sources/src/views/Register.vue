<template>
    <b-container>
        <b-row>
            <b-col>
                <b-card class="rounded-0 border-0 shadow-sm" header="Register">
                    <b-card-body>
                        <b-row class="justify-content-center">
                        <b-form @submit.stop.prevent style="min-width: 30rem">
                            <b-form-group label="Username">
                                <b-form-input ref="username" id="username" type="username" v-model="username" required autofocus></b-form-input>
                            </b-form-group>
                            <b-form-group label="Password">
                                <b-form-input v-on:change="passwordChange" ref="password" id="password" type="password" class="form-control" v-model="password" required></b-form-input>
                            </b-form-group>
                            <b-form-group label="Repeat password">
                                <b-form-input v-on:change="passwordChange" ref="repeatPassword" id="repeatPassword" type="password" class="form-control" v-model="repeatPassword" required></b-form-input>
                            </b-form-group>
                            <b-form-row class="justify-content-end">
                                <b-button class="" type="primary" v-on:click="(e)=>handleSubmit(e)">
                                    Register
                                </b-button>
                            </b-form-row>
                        </b-form>

                        </b-row>
                    </b-card-body>
                </b-card>
            </b-col>
        </b-row>
    </b-container>
</template>

<script>
    export default {
        name: "Register",
        data() {
            return {username:"",password:"", repeatPassword:""};
        },
        methods: {
            handleSubmit()
            {
                if (this.password !== this.repeatPassword || this.password.length <= 0)
                {
                    return alert('Passwords do not match');
                }
                let username = this.username;
                let password = this.password;
                let repeatPassword = this.repeatPassword;
                window.axios.post('/api/register', {username, password, repeatPassword}).then(() => {
                    this.$router.push({name:"login"});
                });
            },
            passwordChange()
            {
                if (this.password !== this.repeatPassword && this.password.length >= 0)
                {
                    if(this.$refs["password"].classList.contains("is-valid"))
                    {
                        this.$refs["password"].classList.remove("is-valid");
                        this.$refs["password-confirm"].classList.remove("is-valid");
                    }
                    if( ! this.$refs["password"].classList.contains("is-invalid"))
                    {
                        this.$refs["password"].classList.add("is-invalid");
                        this.$refs["password-confirm"].classList.add("is-invalid");
                    }
                } else if(this.password === this.repeatPassword && this.password.length >= 8)
                {
                    if( ! this.$refs["password"].classList.contains("is-valid"))
                    {
                        this.$refs["password"].classList.add("is-valid");
                        this.$refs["password-confirm"].classList.add("is-valid");
                    }
                    if(this.$refs["password"].classList.contains("is-invalid"))
                    {
                        this.$refs["password"].classList.remove("is-invalid");
                        this.$refs["password-confirm"].classList.remove("is-invalid");
                    }
                } else
                {
                    if(this.$refs["password"].classList.contains("is-valid"))
                    {
                        this.$refs["password"].classList.remove("is-valid");
                        this.$refs["password_confirmation"].classList.remove("is-valid");
                    }
                    if(this.$refs["password"].classList.contains("is-invalid"))
                    {
                        this.$refs["password"].classList.remove("is-invalid");
                        this.$refs["password_confirmation"].classList.remove("is-invalid");
                    }
                }
            }
        }
    }
</script>

<style scoped>

</style>
