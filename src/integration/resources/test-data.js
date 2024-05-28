db = db.getSiblingDB('testDatabase');

db.users.insertMany([
    {
        id: "#",
        role: "USER",
        username: "testUser",
        password: "password",
        email: "email@email.pl",
        user_preferences: {
            portions: 2,
            diet: "wege",
            disliked_products: [""]
        },
        user_recipes: [""],
        plan_history: [
            {
                day: new Date(2023, 12, 13),
                recipe: {
                    _id: ObjectId('6577660abbac733a111c9424'),
                    name: "Koktajl owocowy",
                    portions: 1,
                    prepare_time: 10,
                    diet: "wegetariańska",
                    ingredients: [
                        { name: "banan", amount: 1, unit: "szt" },
                        { name: "truskawki", amount: 100, unit: "g" },
                        { name: "kiwi", amount: 1, unit: "szt" },
                        { name: "sok pomarańczowy", amount: 150, unit: "ml" },
                        { name: "jogurt naturalny", amount: 50, unit: "g" }
                    ],
                    steps: ["Włóż owoce do blendera", "Dodaj sok i jogurt", "Miksuj do uzyskania gładkiego koktajlu", "Gotowe do picia"]
                }
            },
        ],
        grocery_list:[]
    }
]);

db.recipes.insertMany([
    {
        _id: ObjectId('6577660abbac733a111c9421'),
        name: "Kasza jaglana z warzywami",
        portions: 4,
        prepare_time: 30,
        max_storage_time: 2,
        diet: "wegetariańska",
        ingredients: [
            { name: "kasza jaglana", amount: 200, unit: "g" },
            { name: "marchew", amount: 2, unit: "szt" },
            { name: "brokuł", amount: 150, unit: "g" },
            { name: "oliwa z oliwek", amount: 30, unit: "ml" }
        ],
        steps: ["Ugotuj kaszę", "Pokrój warzywa", "Smaż warzywa na oliwie", "Podawaj razem"]
    },
    {
        _id: ObjectId('6577660abbac733a111c9422'),
        name: "Sałatka owocowa",
        portions: 3,
        prepare_time: 15,
        max_storage_time: 1,
        diet: "wegetariańska",
        ingredients: [
            { name: "jabłko", amount: 2, unit: "szt" },
            { name: "kiwi", amount: 3, unit: "szt" },
            { name: "truskawki", amount: 150, unit: "g" },
            { name: "miód", amount: 20, unit: "ml" }
        ],
        steps: ["Pokrój owoce", "Połącz z miodem", "Delikatnie wymieszaj", "Gotowe do podania"]
    },
    {
        _id: ObjectId('6577660abbac733a111c9423'),
        name: "Jajecznica z warzywami",
        portions: 2,
        prepare_time: 15,
        max_storage_time: 1,
        diet: "wegetariańska",
        ingredients: [
            { name: "jajko", amount: 4, unit: "szt" },
            { name: "papryka", amount: 1, unit: "szt" },
            { name: "pomidor", amount: 2, unit: "szt" },
            { name: "cebula", amount: 1, unit: "szt" },
            { name: "oliwa z oliwek", amount: 20, unit: "ml" }
        ],
        steps: ["Ubij jajka", "Pokrój warzywa", "Smaż warzywa, dodaj jajka", "Podawaj gorące"]
    },
    {
        _id: ObjectId('6577660abbac733a111c9424'),
        name: "Koktajl owocowy",
        portions: 1,
        prepare_time: 10,
        max_storage_time: 1,
        diet: "wegetariańska",
        ingredients: [
            { name: "banan", amount: 1, unit: "szt" },
            { name: "truskawki", amount: 100, unit: "g" },
            { name: "kiwi", amount: 1, unit: "szt" },
            { name: "sok pomarańczowy", amount: 150, unit: "ml" },
            { name: "jogurt naturalny", amount: 50, unit: "g" }
        ],
        steps: ["Włóż owoce do blendera", "Dodaj sok i jogurt", "Miksuj do uzyskania gładkiego koktajlu", "Gotowe do picia"]
    },
    {
        _id: ObjectId('6577660abbac733a111c9425'),
        name: "Ryż z warzywami i kurczakiem",
        portions: 3,
        prepare_time: 25,
        max_storage_time: 2,
        diet: "mięsna",
        ingredients: [
            { name: "ryż", amount: 300, unit: "g" },
            { name: "kurczak", amount: 250, unit: "g" },
            { name: "marchew", amount: 2, unit: "szt" },
            { name: "brokuł", amount: 150, unit: "g" },
            { name: "sos sojowy", amount: 30, unit: "ml" }
        ],
        steps: ["Ugotuj ryż", "Pokrój kurczaka i warzywa", "Smaż kurczaka i warzywa, dodaj sos sojowy", "Podawaj razem z ryżem"]
    }
]);

db.products.insertMany([
    {
        _id: ObjectId('658c088e98487458f640453b'),
        name: "kasza jaglana",
        packing_units: ["g","kg"],
        main_unit: "g",
        packing_measures: [400,1000]
    },
    {
        _id: ObjectId('658c088e98487458f640453c'),
        name: "marchew",
        packing_units: ["g","kg","szt"],
        main_unit: "g",
        packing_measures: [100]
    },
    {
        _id: ObjectId('658c088e98487458f640453d'),
        name: "brokuł",
        packing_units: ["g","kg","szt"],
        main_unit: "g",
        packing_measures: [150]
    },
    {
        _id: ObjectId('658c088e98487458f640453e'),
        name: "oliwa z oliwek",
        packing_units: ["ml","l"],
        main_unit: "ml",
        packing_measures: [50,250,1000]
    },
    {
        _id: ObjectId('659d8d69d6d8ec0007e14d76'),
        name: "jajko",
        packing_units: ["szt"],
        main_unit: "szt",
        packing_measures: [6,10]
    },
    {
        _id: ObjectId('65a43bcc6b150200075ba406'),
        name: "ryż",
        packing_units: ["g", "kg"],
        main_unit: "g",
        packing_measures: [400, 1000]
    },
    {
        _id: ObjectId('65a43be46b150200075ba407'),
        name: "kurczak",
        packing_units: ["g", "kg"],
        main_unit: "g",
        packing_measures: [400, 600]
    },
    {
        _id: ObjectId('65a43bfc6b150200075ba408'),
        name: "sos sojowy",
        packing_units: ["ml", "l"],
        main_unit: "ml",
        packing_measures: [150]
    },
]);



