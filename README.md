
The library expose an interface to publish Glob and to register for changes on Glob using etcd. It is usefull to propagate
configuration or low change state to/from microservice. (I do not used it to cache data like with a Redis)

It use the annotation PathIndex_ to create the key from the globs ().
The first part of the key is the name of the GlobType.
It is then possible to listen for all changes on a GlobType or a sub part of the key (in the given order : is is not
possible to listen for only the last part of a key)

The value can be a json serialisation or a binary serialisation using Globs-bin-serialisation (so only if all field have
the FieldNumber_ annotation)

```
        Client client = Client.builder().endpoints(ETCD).build();

        SharedDataAccess etcDSharedDataAccess = EtcDSharedDataAccess.createBin(client);

        CompletableFuture<Glob> done = new CompletableFuture<>();
        etcDSharedDataAccess.listen(Data1.TYPE, new SharedDataAccess.Listener() {
            public void put(Glob glob) {
                try {
                    etcDSharedDataAccess.get(glob.getType(), glob).join();
                    done.complete(glob);
                } catch (Exception e) {
                    done.complete(null);
                }
            }

            public void delete(Glob glob) {

            }
        }, Data1.TYPE.instantiate()
                .set(Data1.shop, "mg.free.fr")
                .set(Data1.workerName, "w1")
                .set(Data1.num, 1));

        MutableGlob data = Data1.TYPE.instantiate()
                .set(Data1.shop, "mg.free.fr")
                .set(Data1.workerName, "w1")
                .set(Data1.num, 1)
                .set(Data1.someData, "blabla");

        // publish data.
        etcDSharedDataAccess.register(data)
                .get(1, TimeUnit.MINUTES);

        final Glob join = done.join();
        Assert.assertNotNull(join);
        Assert.assertEquals("blabla", join.get(Data1.someData));
        etcDSharedDataAccess.end();
```

