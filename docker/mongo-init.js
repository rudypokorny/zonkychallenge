db.createUser ({
    user: "zonky",
    pwd: "zonky",
    roles: [{
        role: "readWrite",
        db: "loans"
    }]
})