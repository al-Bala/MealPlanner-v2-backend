db.createUser(
    {
        user: "admin",
        pwd: "admin",
        roles: [
            {
                role: "readWrite",
                db: "mealplanner"
            }
        ]
    }
)

db.users.insertMany([
    {
        _id: ObjectId('222222222222222222222222'),
        username: 'admin',
        email: 'admin@admin.pl',
        password: '$2a$10$tR8zusPyujdG.PgOg6ikM.DGxc//Y1F3GnqVMrjUQhF702iZSLDd6',
        preferences: {
            diet: '',
            portions: 0,
            products_to_avoid: ['']
        },
        plans: [
            {
                days: [
                    {
                        date: '',
                        planned_day: [
                            {
                                type_of_meal: '',
                                recipeId: {
                                    $oid: ''
                                },
                                recipeName: ''
                            }
                        ]
                    }
                ],
            }
        ]
    }
])

db.units.insertMany([
    {
        _id: ObjectId('66696fb85a17b49d3cbfa102'),
        label: "weight",
        units: ["g", "dag", "kg"]
    },
    {
        _id: ObjectId('666970135a17b49d3cbfa105'),
        label: "capacity",
        units: ["ml", "l"]
    },
    {
        _id: ObjectId('6669701c5a17b49d3cbfa107'),
        label: "piece",
        units: ["pc."]
    },
    {
        _id: ObjectId('668442bad6451179fcea88c3'),
        label: "measuring",
        units: ["cup", "tbsp", "tsp"]
    },
])

db.recipes.insertMany([
    {
        _id: ObjectId('111111111111111111111111'),
        name: "Recipe not found",
        type_of_meal: ["BREAKFAST", "DINNER", "SUPPER"],
        portions: 1,
        prepare_time: 1000000,
        max_storage_time: 1000000,
        diet: "",
        ingredients: [{
            name: "ing", amount: 100, unit: "g"
        }],
        steps: []
    },
    {
        _id: ObjectId('6577660abbac733a111c9421'),
        name: "Kasza jaglana z warzywami",
        type_of_meal: ["DINNER"],
        portions: 2,
        prepare_time: 30,
        max_storage_time: 2,
        diet: "wegetariańska",
        ingredients: [
            { name: "kasza jaglana", amount: 200, unit: "g" },
            { name: "marchew", amount: 200, unit: "g" },
            { name: "brokul", amount: 150, unit: "g" },
            { name: "oliwa z oliwek", amount: 30, unit: "ml" }
        ],
        steps: ["Ugotuj kaszę", "Pokrój warzywa", "Smaż warzywa na oliwie", "Podawaj razem"]
    },
    {
        _id: ObjectId('6577660abbac733a111c9422'),
        name: "Sałatka owocowa",
        type_of_meal: ["BREAKFAST", "SUPPER"],
        portions: 3,
        prepare_time: 15,
        max_storage_time: 1,
        diet: "wegetariańska",
        ingredients: [
            { name: "jabłko", amount: 160, unit: "g" },
            { name: "kiwi", amount: 150, unit: "g" },
            { name: "truskawki", amount: 120, unit: "g" },
            { name: "miód", amount: 20, unit: "ml" }
        ],
        steps: ["Pokrój owoce", "Połącz z miodem", "Delikatnie wymieszaj", "Gotowe do podania"]
    },
    {
        _id: ObjectId('6577660abbac733a111c9423'),
        name: "Jajecznica z warzywami",
        type_of_meal: ["BREAKFAST"],
        portions: 2,
        prepare_time: 15,
        max_storage_time: 1,
        diet: "wegetariańska",
        ingredients: [
            { name: "jajko", amount: 4, unit: "piece" },
            { name: "papryka", amount: 140, unit: "g" },
            { name: "pomidor", amount: 180, unit: "g" },
            { name: "cebula", amount: 80, unit: "g" },
            { name: "oliwa z oliwek", amount: 20, unit: "ml" }
        ],
        steps: ["Ubij jajka", "Pokrój warzywa", "Smaż warzywa, dodaj jajka", "Podawaj gorące"]
    },
    {
        _id: ObjectId('6577660abbac733a111c9424'),
        name: "Koktajl owocowy",
        type_of_meal: ["BREAKFAST", "SUPPER"],
        portions: 1,
        prepare_time: 10,
        max_storage_time: 1,
        diet: "wegetariańska",
        ingredients: [
            { name: "banan", amount: 70, unit: "g" },
            { name: "truskawki", amount: 100, unit: "g" },
            { name: "kiwi", amount: 50, unit: "g" },
            { name: "sok pomarańczowy", amount: 150, unit: "ml" },
            { name: "jogurt naturalny", amount: 50, unit: "g" }
        ],
        steps: ["Włóż owoce do blendera", "Dodaj sok i jogurt", "Miksuj do uzyskania gładkiego koktajlu", "Gotowe do picia"]
    },
    {
        _id: ObjectId('6577660abbac733a111c9425'),
        name: "Ryż z warzywami i kurczakiem",
        type_of_meal: ["DINNER"],
        portions: 3,
        prepare_time: 25,
        max_storage_time: 2,
        diet: "mięsna",
        ingredients: [
            { name: "ryż", amount: 300, unit: "g" },
            { name: "kurczak", amount: 250, unit: "g" },
            { name: "marchew", amount: 200, unit: "g" },
            { name: "brokul", amount: 150, unit: "g" },
            { name: "sos sojowy", amount: 30, unit: "ml" }
        ],
        steps: ["Ugotuj ryż", "Pokrój kurczaka i warzywa", "Smaż kurczaka i warzywa, dodaj sos sojowy", "Podawaj razem z ryżem"]
    },
    {
        _id: ObjectId('6577660abbac733a111c9427'),
        name: "Makaron z warzywami i pesto",
        type_of_meal: ["DINNER"],
        portions: 4,
        prepare_time: 20,
        max_storage_time: 3,
        diet: "wegetariańska",
        ingredients: [
            { name: "makaron", amount: 400, unit: "g" },
            { name: "cukinia", amount: 1, unit: "szt" },
            { name: "papryka", amount: 120, unit: "g" },
            { name: "cebula", amount: 80, unit: "g" },
            // { name: "czosnek", amount: 2, unit: "ząbki" },
            { name: "pesto bazyliowe", amount: 100, unit: "g" },
            { name: "oliwa z oliwek", amount: 30, unit: "ml" }
        ],
        steps: ["Ugotuj makaron al dente", "Pokrój cukinię, paprykę i cebulę", "Smaż cebulę i czosnek na oliwie, dodaj warzywa", "Dodaj pomidorki koktajlowe i smaż chwilę", "Wymieszaj warzywa z makaronem i pesto", "Podawaj natychmiast"]
    }
]);

db.products.insertMany([
    {
        _id: ObjectId('66ef164c13cafc00077199f1'),
        name: "ing",
        packing_units: ["weight"],
        main_unit: "g",
        packing_sizes: [100],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('66a50ea4bdacf34ef3f53655'),
        name: "makaron",
        packing_units: ["weight"],
        main_unit: "g",
        packing_sizes: [500,1000],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('66a50ebfbdacf34ef3f53657'),
        name: "cukinia",
        packing_units: ["weight", "szt."],
        main_unit: "g",
        standard_weight: 300,
        packing_sizes: [0],
        max_days_after_opening: 3
    },
    {
        _id: ObjectId('66a50ebfbdacf34ef3f53658'),
        name: "pesto bazyliowe",
        packing_units: ["weight"],
        main_unit: "g",
        packing_sizes: [250,500],
        max_days_after_opening: 10
    },
    {
        _id: ObjectId('658c088e98487458f640453b'),
        name: "kasza jaglana",
        packing_units: ["weight", "measuring"],
        main_unit: "g",
        packing_sizes: [400,1000],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('658c088e98487458f640453c'),
        name: "marchew",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 100,
        packing_sizes: [0],
        max_days_after_opening: 5
    },
    {
        _id: ObjectId('658c088e98487458f640453d'),
        name: "brokul",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 150,
        packing_sizes: [0],
        max_days_after_opening: 5
    },
    {
        _id: ObjectId('658c088e98487458f640453e'),
        name: "oliwa z oliwek",
        packing_units: ["capacity", "measuring"],
        main_unit: "ml",
        packing_sizes: [50,250,1000],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('6669d0e85a17b49d3cbfa10c'),
        name: "jabłko",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 80,
        packing_sizes: [0],
        max_days_after_opening: 3
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa10e'),
        name: "kiwi",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 50,
        packing_sizes: [0],
        max_days_after_opening: 3
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa10f'),
        name: "truskawki",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        packing_sizes: [250, 500, 1000],
        max_days_after_opening: 4
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa110'),
        name: "miód",
        packing_units: ["capacity", "measuring"],
        main_unit: "ml",
        packing_sizes: [500],
        max_days_after_opening: 60
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa111'),
        name: "banan",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 100,
        packing_sizes: [0],
        max_days_after_opening: 3
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa112'),
        name: "sok pomarańczowy",
        packing_units: ["capacity"],
        main_unit: "ml",
        packing_sizes: [250, 500, 1000],
        max_days_after_opening: 8
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa113'),
        name: "jogurt naturalny",
        packing_units: ["capacity"],
        main_unit: "ml",
        packing_sizes: [180, 250, 300],
        max_days_after_opening: 4
    },
    {
        _id: ObjectId('659d8d69d6d8ec0007e14d76'),
        name: "jajko",
        packing_units: ["piece"],
        main_unit: "piece",
        packing_sizes: [6,10],
        max_days_after_opening: 1
    },
    {
        _id: ObjectId('65a43bcc6b150200075ba406'),
        name: "ryż",
        packing_units: ["weight", "measuring"],
        main_unit: "g",
        packing_sizes: [400, 1000],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('65a43be46b150200075ba407'),
        name: "kurczak",
        packing_units: ["weight"],
        main_unit: "g",
        packing_sizes: [400, 600],
        max_days_after_opening: 4
    },
    {
        _id: ObjectId('65a43bfc6b150200075ba408'),
        name: "sos sojowy",
        packing_units: ["capacity", "measuring"],
        main_unit: "ml",
        packing_sizes: [150],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('65a980d8d5a2820007ae5a42'),
        name: "papryka",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 120,
        packing_sizes: [0],
        max_days_after_opening: 5
    },
    {
        _id: ObjectId('65a98106d5a2820007ae5a43'),
        name: "pomidor",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 100,
        packing_sizes: [0],
        max_days_after_opening: 5
    },
    {
        _id: ObjectId('65a9813ad5a2820007ae5a44'),
        name: "cebula",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 80,
        packing_sizes: [0],
        max_days_after_opening: 5
    },
]);

