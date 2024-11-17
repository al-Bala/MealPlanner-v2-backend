db = db.getSiblingDB('testDatabase');

db.users.insertMany([
    {
        _id: ObjectId('6672e2e8be614a1616e7552f'),
        // role: "USER",
        username: "testUser",
        password: "$2a$10$jqMDdOlw8P60m686b1Q.GeLjpdPncHUVJiXZUSq0QNFxoyMe74q86",
        email: "email@email.pl",
        preferences: {
            dietId: "66f7f883326bd5fde1b7f779",   // with meat
            portions: 2,
            products_to_avoid: ["olives"]
        },
        // user_recipes: [""],
        plans: [
            {
                plannedDays: [
                    {
                        // date: ISODate('2024-09-12T22:00:00.000Z'),
                        date: "2024-09-12",
                        plannedRecipes: [
                            {
                                typeOfMeal: 'DINNER-ID',
                                recipeId: '6577660abbac733a111c9425',
                                recipeName: 'Rice with vegetables and chicken',
                                forHowManyDays: 1
                            }
                        ]
                    },
                    {
                        // date: ISODate('2024-09-13T22:00:00.000Z'),
                        date: "2024-09-13",
                        plannedRecipes: [
                            {
                                typeOfMeal: 'BREAKFAST-ID',
                                recipeId: '6577660abbac733a111c9423',
                                recipeName: 'Scrambled eggs with vegetables',
                                forHowManyDays: 1
                            }
                        ]
                    }
                ]
            }
        ]
    }
]);

db.recipes.insertMany([
    {
        _id: ObjectId('111111111111111111111111'),
        name: "Recipe not found",
        type_of_meal: ["BREAKFAST-ID", "DINNER-ID", "SUPPER-ID"],
        portions: 1,
        prepare_time: 1000000,
        max_storage_time: 1000000,
        dietId: "",
        ingredients: [{
            name: "ing", amount: 100, unit: "g"
        }],
        steps: []
    },
    {
        _id: ObjectId('6577660abbac733a111c9421'),
        // name: "Kasza jaglana z warzywami",
        name: "Millet groats with vegetables",
        type_of_meal: ["DINNER-ID"],
        portions: 2,
        prepare_time: 30,
        max_storage_time: 2,
        dietId: "66f7f86a326bd5fde1b7f775",
        ingredients: [
            { name: "millet groats", amount: 200, unit: "g" },
            { name: "carrot", amount: 200, unit: "g" },
            { name: "broccoli", amount: 150, unit: "g" },
            { name: "olive oil", amount: 30, unit: "ml" }
        ],
        // steps: ["Ugotuj kaszę", "Pokrój warzywa", "Smaż warzywa na oliwie", "Podawaj razem"]
        steps: []
    },
    {
        _id: ObjectId('6577660abbac733a111c9422'),
        // name: "Sałatka owocowa",
        name: "Fruit salad",
        type_of_meal: ["BREAKFAST-ID", "SUPPER-ID"],
        portions: 3,
        prepare_time: 15,
        max_storage_time: 1,
        dietId: "66f7f86a326bd5fde1b7f775",
        ingredients: [
            { name: "apple", amount: 160, unit: "g" },
            { name: "kiwi", amount: 150, unit: "g" },
            { name: "strawberry", amount: 120, unit: "g" },
            { name: "honey", amount: 20, unit: "ml" }
        ],
        // steps: ["Pokrój owoce", "Połącz z miodem", "Delikatnie wymieszaj", "Gotowe do podania"]
        steps: []
    },
    {
        _id: ObjectId('6577660abbac733a111c9423'),
        // name: "Jajecznica z warzywami",
        name: "Scrambled eggs with vegetables",
        type_of_meal: ["BREAKFAST-ID"],
        portions: 2,
        prepare_time: 15,
        max_storage_time: 1,
        dietId: "66f7f86a326bd5fde1b7f775",
        ingredients: [
            { name: "egg", amount: 4, unit: "piece" },
            { name: "pepper", amount: 140, unit: "g" },
            { name: "tomato", amount: 180, unit: "g" },
            { name: "onion", amount: 80, unit: "g" },
            { name: "olive oil", amount: 20, unit: "ml" }
        ],
        // steps: ["Ubij jajka", "Pokrój warzywa", "Smaż warzywa, dodaj jajka", "Podawaj gorące"]
        steps: []
    },
    {
        _id: ObjectId('6577660abbac733a111c9424'),
        // name: "Koktajl owocowy",
        name: "Fruit cocktail",
        type_of_meal: ["BREAKFAST-ID", "SUPPER-ID"],
        portions: 1,
        prepare_time: 10,
        max_storage_time: 1,
        dietId: "66f7f86a326bd5fde1b7f775",
        ingredients: [
            { name: "banana", amount: 70, unit: "g" },
            { name: "strawberry", amount: 100, unit: "g" },
            { name: "kiwi", amount: 50, unit: "g" },
            { name: "orange juice", amount: 150, unit: "ml" },
            { name: "natural yoghurt", amount: 50, unit: "g" }
        ],
        // steps: ["Włóż owoce do blendera", "Dodaj sok i jogurt", "Miksuj do uzyskania gładkiego koktajlu", "Gotowe do picia"]
        steps: []
    },
    {
        _id: ObjectId('6577660abbac733a111c9425'),
        // name: "Ryż z warzywami i kurczakiem",
        name: "Rice with vegetables and chicken",
        type_of_meal: ["DINNER-ID"],
        portions: 3,
        prepare_time: 25,
        max_storage_time: 2,
        dietId: "66f7f883326bd5fde1b7f779",
        ingredients: [
            { name: "rice", amount: 300, unit: "g" },
            { name: "chicken", amount: 250, unit: "g" },
            { name: "carrot", amount: 200, unit: "g" },
            { name: "broccoli", amount: 150, unit: "g" },
            { name: "soy sauce", amount: 30, unit: "ml" }
        ],
        // steps: ["Ugotuj ryż", "Pokrój kurczaka i warzywa", "Smaż kurczaka i warzywa, dodaj sos sojowy", "Podawaj razem z ryżem"]
        steps: []
    },
    {
        _id: ObjectId('6577660abbac733a111c9427'),
        // name: "Makaron z warzywami i pesto",
        name: "Pasta with vegetables and pesto",
        type_of_meal: ["DINNER-ID"],
        portions: 4,
        prepare_time: 20,
        max_storage_time: 3,
        dietId: "66f7f86a326bd5fde1b7f775",
        ingredients: [
            { name: "pasta", amount: 400, unit: "g" },
            { name: "zucchini", amount: 1, unit: "szt" },
            { name: "pepper", amount: 120, unit: "g" },
            { name: "onion", amount: 80, unit: "g" },
            // { name: "czosnek", amount: 2, unit: "ząbki" },
            { name: "basil pesto", amount: 100, unit: "g" },
            { name: "olive oil", amount: 30, unit: "ml" }
        ],
        // steps: ["Ugotuj makaron al dente", "Pokrój cukinię, paprykę i cebulę", "Smaż cebulę i czosnek na oliwie, dodaj warzywa", "Dodaj pomidorki koktajlowe i smaż chwilę", "Wymieszaj warzywa z makaronem i pesto", "Podawaj natychmiast"]
        steps: []
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
        name: "pasta",
        packing_units: ["weight"],
        main_unit: "g",
        packing_sizes: [500,1000],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('66a50ebfbdacf34ef3f53657'),
        name: "zucchini",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 300,
        packing_sizes: [0],
        max_days_after_opening: 3
    },
    {
        _id: ObjectId('66a50ebfbdacf34ef3f53658'),
        name: "basil pesto",
        packing_units: ["weight"],
        main_unit: "g",
        packing_sizes: [250,500],
        max_days_after_opening: 10
    },
    {
        _id: ObjectId('658c088e98487458f640453b'),
        name: "millet groats",
        packing_units: ["weight", "measuring"],
        main_unit: "g",
        packing_sizes: [400,1000],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('658c088e98487458f640453c'),
        name: "carrot",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 100,
        packing_sizes: [0],
        max_days_after_opening: 5
    },
    {
        _id: ObjectId('658c088e98487458f640453d'),
        name: "broccoli",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 150,
        packing_sizes: [0],
        max_days_after_opening: 5
    },
    {
        _id: ObjectId('658c088e98487458f640453e'),
        name: "olive oil",
        packing_units: ["capacity", "measuring"],
        main_unit: "ml",
        packing_sizes: [50,250,1000],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('6669d0e85a17b49d3cbfa10c'),
        name: "apple",
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
        name: "strawberry",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        packing_sizes: [250, 500, 1000],
        max_days_after_opening: 4
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa110'),
        name: "honey",
        packing_units: ["capacity", "measuring"],
        main_unit: "ml",
        packing_sizes: [500],
        max_days_after_opening: 60
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa111'),
        name: "banana",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 100,
        packing_sizes: [0],
        max_days_after_opening: 3
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa112'),
        name: "orange juice",
        packing_units: ["capacity"],
        main_unit: "ml",
        packing_sizes: [250, 500, 1000],
        max_days_after_opening: 8
    },
    {
        _id: ObjectId('6669d1235a17b49d3cbfa113'),
        name: "natural yoghurt",
        packing_units: ["capacity"],
        main_unit: "ml",
        packing_sizes: [180, 250, 300],
        max_days_after_opening: 4
    },
    {
        _id: ObjectId('659d8d69d6d8ec0007e14d76'),
        name: "egg",
        packing_units: ["piece"],
        main_unit: "piece",
        packing_sizes: [6,10],
        max_days_after_opening: 1
    },
    {
        _id: ObjectId('65a43bcc6b150200075ba406'),
        name: "rice",
        packing_units: ["weight", "measuring"],
        main_unit: "g",
        packing_sizes: [400, 1000],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('65a43be46b150200075ba407'),
        name: "chicken",
        packing_units: ["weight"],
        main_unit: "g",
        packing_sizes: [400, 600],
        max_days_after_opening: 4
    },
    {
        _id: ObjectId('65a43bfc6b150200075ba408'),
        name: "soy sauce",
        packing_units: ["capacity", "measuring"],
        main_unit: "ml",
        packing_sizes: [150],
        max_days_after_opening: 30
    },
    {
        _id: ObjectId('65a980d8d5a2820007ae5a42'),
        name: "pepper",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 120,
        packing_sizes: [0],
        max_days_after_opening: 5
    },
    {
        _id: ObjectId('65a98106d5a2820007ae5a43'),
        name: "tomato",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 100,
        packing_sizes: [0],
        max_days_after_opening: 5
    },
    {
        _id: ObjectId('65a9813ad5a2820007ae5a44'),
        name: "onion",
        packing_units: ["weight", "piece"],
        main_unit: "g",
        standard_weight: 80,
        packing_sizes: [0],
        max_days_after_opening: 5
    }
]);



